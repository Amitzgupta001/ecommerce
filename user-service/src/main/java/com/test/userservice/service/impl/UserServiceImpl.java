package com.test.userservice.service.impl;

import com.test.userservice.constant.NullChecker;
import com.test.userservice.dto.request.*;
import com.test.userservice.dto.response.TokenResponse;
import com.test.userservice.dto.response.UserResponse;
import com.test.userservice.entity.User;
import com.test.userservice.enums.UserType;
import com.test.userservice.exception.EntityNotFoundException;
import com.test.userservice.exception.NothingToUpdateException;
import com.test.userservice.mapping.UserMapping;
import com.test.userservice.repository.UserRepository;
import com.test.userservice.service.KeyclockService;
import com.test.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private KeyclockService keyclockService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapping userMapping;

    @Override
    public void registerStaff(UserRequest userRequest) {
        
        log.info("Registering staff. Request: {}", userRequest);

        userRequest.setUserType(UserType.STAFF);
        registerUser(userRequest);

        
        log.info("Staff registered successfully.");
    }

    @Override
    public void registerCustomer(UserRequest userRequest) {
        
        log.info("Registering customer. Request: {}", userRequest);

        userRequest.setUserType(UserType.CUSTOMER);
        registerUser(userRequest);
        
        log.info("Customer registered successfully.");
    }


    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        
        log.info("Signing in.");

        if(!"admin".equals(signInRequest.getEmail())) {
            Optional<User> userOptional = userRepository.findByEmail(signInRequest.getEmail());
            if (userOptional.isEmpty()) {
                throw new EntityNotFoundException("User doesn't exist");
            }
        }
        TokenResponse tokenResponse = keyclockService.fetchAccessToken(signInRequest);

        
        log.info("Sign in successful." );

        return tokenResponse;
    }

    @Override
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        
        log.info("Refreshing token. ");

        TokenResponse tokenResponse = keyclockService.refreshToken(tokenRefreshRequest);

        
        log.info("Token refreshed successfully.");

        return tokenResponse;
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        
        log.info("Fetching user by email. Email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException("User doesn't exist");
        }
        UserResponse userResponse = userMapping.entityToResponse(userOptional.get());

        
        log.info("User fetched successfully by email. Response: {}", userResponse);

        return userResponse;
    }

    @Override
    public void updateUser(UpdateUserRequest updateRequest, HttpServletRequest request) {
        
        log.info("Updating user. Request: {}", updateRequest);

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String email = accessToken.getEmail();
        validateUser(email, updateRequest);
        User existUser = userRepository.findByEmail(email).get();
        User user = userMapping.updateUser(existUser, updateRequest);
        userRepository.save(user);

        log.info("User updated successfully.");
    }

    @Override
    public void updatedPassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        
        log.info("Updating password.");

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String email = accessToken.getEmail();

        TokenResponse tokenResponse = keyclockService.fetchAccessToken(SignInRequest.builder().email(email).password(updatePasswordRequest.getOldPassword()).build());

        if(tokenResponse == null || tokenResponse.getAccessToken() == null){
            throw new RuntimeException("Password entered is wrong");
        }
        keyclockService.updatePassword(email, updatePasswordRequest);

        log.info("Password updated successfully.");
    }

    @Override
    public UserResponse getUserByToken(HttpServletRequest authorization) {
        
        log.info("Fetching user by token.");

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authorization.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String email = accessToken.getEmail();
        UserResponse userResponse = this.getUserByEmail(email);
        
        log.info("User fetched successfully by token. Response: {}", userResponse);

        return userResponse;
    }

    @Override
    public void logoutUser(HttpServletRequest request) {
        keyclockService.logout(request);

    }

    private void validateUser(String email, UpdateUserRequest userRequest) {
        
        log.info("Validating user. Email: {}, Request: {}", email, userRequest);

        Optional<User> isExists = userRepository.findByEmail(email);
        if(isExists.isEmpty()){
            throw new EntityNotFoundException("User Not Found");
        }

        if(NullChecker.allNull(userRequest)){
            throw new NothingToUpdateException("All fields are null in the request");
        }

        
        log.info("User validated successfully.");
    }

    private void validateUserRegistration(String email) {

        log.info("Validating user. Email: {}", email);

        Optional<User> isExists = userRepository.findByEmail(email);
        if(isExists.isPresent()){
            throw new BadRequestException("User already exist Found");
        }

        log.info("User validated successfully.");
    }


    private void registerUser(UserRequest userRequest) {
        
        log.info("Registering user. Request: {}", userRequest);
        validateUserRegistration(userRequest.getEmail());

        Response response = keyclockService.registerUser(userRequest);
        log.info("Response | Status: {} | Status Info: {}", response.getStatus(), response.getStatusInfo());
        if (response.getStatus() == 201) {
            String keyclockUserId = CreatedResponseUtil.getCreatedId(response);
            log.info("Created userId {} in Keycloak", keyclockUserId);
            User userEntity = userMapping.requestToEntity(userRequest);
            userEntity.setKeyclockUserId(keyclockUserId);
            User savedUser = userRepository.save(userEntity);
        } else {
            throw new RuntimeException("Error in processing Keycloak request");
        }
        log.info("User registered successfully.");
    }
}
