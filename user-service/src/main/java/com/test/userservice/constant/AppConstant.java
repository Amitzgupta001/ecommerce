package com.test.userservice.constant;

public interface AppConstant {

    String[] WHITE_LIST_ENDPOINT = {
            "/api/v1/user/register","/api/v1/user/registerStaff","/api/v1/user/signin","/api/v1/user/refreshToken",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"};

    String GRANT_TYPE = "grant_type";
    String SUBJECT_TOKEN_TYPE = "subject_token_type";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String REFRESH_TOKEN = "refresh_token";

    String SUBJECT_TOKEN = "subject_token";

    String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";

    String TOKEN_ENDPOINT = "/realms/ecommerce-realm/protocol/openid-connect/token";

}
