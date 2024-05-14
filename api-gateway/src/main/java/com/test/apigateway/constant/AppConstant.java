package com.test.apigateway.constant;

public interface AppConstant {
    String[] WHITE_LIST_ENDPOINT = {
            "/product-service/swagger-ui/**", "/product-service/v3/api-docs/**",
            "/user-service/swagger-ui/**", "/user-service/v3/api-docs/**",
            "/order-service/swagger-ui/**", "/order-service/v3/api-docs/**",
            "/product-service/api/v1/category","/product-service/api/v1/product/**",
            "/user-service/api/v1/user/register","/user-service/api/v1/user/registerStaff",
            "/user-service/api/v1/user/signin", "/user-service/api/v1/user/refreshToken"};

    String[] GET_WHITE_LIST_ENDPOINT = {
            "/product-service/api/v1/category/**","/product-service/api/v1/inventory/**",
            "/product-service/api/v1/products/**",};
}
