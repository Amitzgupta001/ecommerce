package com.test.userservice.service;


import com.test.userservice.dto.request.SignInRequest;
import com.test.userservice.dto.request.TokenRefreshRequest;
import com.test.userservice.dto.request.UpdatePasswordRequest;
import com.test.userservice.dto.request.UserRequest;
import com.test.userservice.dto.response.TokenResponse;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface KeyclockService {
    Response registerUser(UserRequest userRequest);

    TokenResponse fetchAccessToken(SignInRequest request) throws JSONException;

    TokenResponse refreshToken(TokenRefreshRequest request);

    void updatePassword(String email, UpdatePasswordRequest updatePasswordRequest);

    void logout(HttpServletRequest request);
}
