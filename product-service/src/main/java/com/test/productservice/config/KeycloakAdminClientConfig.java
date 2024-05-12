package com.test.productservice.config;


import org.springframework.beans.factory.annotation.Value;
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

}
