package com.test.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;

    private Long expiresIn;

    private Long refreshExpiresIn;

    private String refreshToken;

    private String scope;
}
