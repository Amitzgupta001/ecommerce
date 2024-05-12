package com.test.productservice.constants;

public interface AppConstant {
    String[] WHITE_LIST_ENDPOINT = {
            "/swagger-ui/**", "/v3/api-docs/**"};

    String[] GET_WHITE_LIST_ENDPOINT = {
            "/api/v1/category/**","/api/v1/inventory/**",
            "/api/v1/product/**",};

}
