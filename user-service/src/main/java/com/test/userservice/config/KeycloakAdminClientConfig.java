package com.test.userservice.config;


import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RealmEventsConfigRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    @Value("${keycloak.credentials.secret}")
    private String secretKey;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;
    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder().
                serverUrl(authUrl).realm(realm).
                grantType(OAuth2Constants.CLIENT_CREDENTIALS).
                clientId(clientId).clientSecret(secretKey).
                resteasyClient(new ResteasyClientBuilder().
                        connectionPoolSize(10).build())
                .build();
    }

    public UsersResource getUserResource() {
        return keycloak().realm(realm).users();
    }

    public RealmEventsConfigRepresentation getKeyCloakConfiguration() {
        return keycloak().realm(realm).getRealmEventsConfig();
    }

    public String getClientId() {
        return keycloak().realm(realm).clients().findByClientId(clientId).get(0).getId();
    }


}
