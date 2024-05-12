package com.test.userservice.service;

import com.test.userservice.dto.request.*;
import com.test.userservice.dto.response.TokenResponse;
import com.test.userservice.dto.response.UserResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void registerStaff(UserRequest userRequest);

    void registerCustomer(UserRequest userRequest);

    TokenResponse signIn(SignInRequest signInRequest);

    TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);

    UserResponse getUserByEmail(String email);

    void updateUser(UpdateUserRequest updateRequest, HttpServletRequest request);

    void updatedPassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request);

    UserResponse getUserByToken(HttpServletRequest authorization);

    void logoutUser(HttpServletRequest request);
}
