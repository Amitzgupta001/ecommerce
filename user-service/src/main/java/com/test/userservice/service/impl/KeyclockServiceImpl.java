package com.test.userservice.service.impl;

import com.test.userservice.config.KeycloakAdminClientConfig;
import com.test.userservice.constant.AppConstant;
import com.test.userservice.dto.request.SignInRequest;
import com.test.userservice.dto.request.TokenRefreshRequest;
import com.test.userservice.dto.request.UpdatePasswordRequest;
import com.test.userservice.dto.request.UserRequest;
import com.test.userservice.dto.response.TokenRefreshResponse;
import com.test.userservice.dto.response.TokenResponse;
import com.test.userservice.service.KeyclockService;
import org.json.JSONException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyclockServiceImpl implements KeyclockService {

    @Autowired
    private KeycloakAdminClientConfig keycloakAdminClientConfig;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.credentials.secret}")
    private String secretKey;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response registerUser(UserRequest userRequest) {
        Keycloak keycloak = keycloakAdminClientConfig.keycloak();
        keycloak.tokenManager().getAccessToken();
        UserRepresentation user = new UserRepresentation();

        user.setEnabled(true);
        user.setUsername(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setEmailVerified(true);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userRequest.getPassword());
        user.setCredentials(List.of(passwordCred));

        UsersResource usersResource = keycloakAdminClientConfig.getUserResource();
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            String keyclockUserId = CreatedResponseUtil.getCreatedId(response);
            assignRoleToUser(keyclockUserId, userRequest.getUserType().name());
        } else {
            throw new RuntimeException("Error in processinf Keyclock request");
        }
        return response;
    }

    private void assignRoleToUser(String userId, String role) {
        Keycloak keycloak = keycloakAdminClientConfig.keycloak();
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId);

        //getting client
        ClientRepresentation clientRepresentation = keycloak.realm(realm).clients().findAll().stream().filter(client -> client.getClientId().equals(clientId)).collect(Collectors.toList()).get(0);
        ClientResource clientResource = keycloak.realm(realm).clients().get(clientRepresentation.getId());
        //getting role
        RoleRepresentation roleRepresentation = clientResource.roles().list().stream().filter(element -> element.getName().equals(role)).collect(Collectors.toList()).get(0);
        //assigning to user
        userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
    }

    @Override
    public TokenResponse fetchAccessToken(SignInRequest request) throws JSONException {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", secretKey);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse response = new AccessTokenResponse();
        try {
            response =
                    authzClient.obtainAccessToken(request.getEmail(), request.getPassword());
        }
        catch (HttpResponseException e) {
            throw new RuntimeException("Error in procession keyclock signin");
        }
        TokenResponse.TokenResponseBuilder keycloakResponse = TokenResponse.builder();
        keycloakResponse.accessToken(response.getToken());
        keycloakResponse.expiresIn(response.getExpiresIn());
        keycloakResponse.refreshExpiresIn(response.getRefreshExpiresIn());
        keycloakResponse.refreshToken(response.getRefreshToken());
        keycloakResponse.scope(response.getScope()).build();
        return keycloakResponse.build();
    }

    @Override
    public TokenResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(AppConstant.GRANT_TYPE, AppConstant.REFRESH_TOKEN);
        map.add(AppConstant.CLIENT_ID, clientId);
        map.add(AppConstant.CLIENT_SECRET, secretKey);
        map.add(AppConstant.REFRESH_TOKEN, requestRefreshToken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<TokenRefreshResponse> response =
                restTemplate.exchange(authUrl + AppConstant.TOKEN_ENDPOINT, HttpMethod.POST, entity, TokenRefreshResponse.class);
        TokenRefreshResponse tokenRes = response.getBody();
        TokenResponse token = TokenResponse.builder().accessToken(tokenRes.getAccessToken()).refreshToken(tokenRes.getRefreshToken())
                .expiresIn(tokenRes.getExpiresIn()).refreshExpiresIn(tokenRes.getRefreshExpiresIn()).scope(tokenRes.getScope()).build();
        return token;
    }

    @Override
    public void updatePassword(String email, UpdatePasswordRequest updatePasswordRequest) {
        UsersResource userResource = keycloakAdminClientConfig.getUserResource();
        List<UserRepresentation> userRepresentations = userResource.search(email);
        if(userRepresentations == null || userRepresentations.isEmpty()){
            throw new RuntimeException("User not found in keyclock");
        }
        UserRepresentation userRepresentation = userRepresentations.get(0);
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(updatePasswordRequest.getNewPassword());
        userRepresentation.setCredentials(List.of(passwordCred));
        userResource.get(userRepresentation.getId()).update(userRepresentation);
    }

    @Override
    public void logout(HttpServletRequest request) {
        UsersResource usersResource = keycloakAdminClientConfig.getUserResource();
        try {

            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
            KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
            KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();
            String email = accessToken.getEmail();
            List<UserRepresentation> userRepresentations = keycloakAdminClientConfig.getUserResource().search(email);
            if(userRepresentations == null || userRepresentations.isEmpty()){
                throw new RuntimeException("User not found in keyclock");
            }
            UserRepresentation userRepresentation = userRepresentations.get(0);
            usersResource.get(userRepresentation.getId()).logout();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
