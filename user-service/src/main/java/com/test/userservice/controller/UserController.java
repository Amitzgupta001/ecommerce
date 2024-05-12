package com.test.userservice.controller;

import com.test.userservice.dto.request.*;
import com.test.userservice.dto.response.CommonResponse;
import com.test.userservice.dto.response.UserResponse;
import com.test.userservice.dto.response.TokenResponse;
import com.test.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private HttpServletRequest request;

    @Operation(summary = " register the customers.", description = "This method is used to  register the customers.", tags = {"user-controller"})
    @PostMapping("/register")
    public CommonResponse<String> registerCustomer(@RequestBody UserRequest userRequest) {
        userService.registerCustomer(userRequest);
        return CommonResponse.<String>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data("Successfully registered").build();
    }

    @Operation(summary = " register the staff.", description = "This method is used to  register the customers.", tags = {"user-controller"})
    @PostMapping("/registerStaff")
    public CommonResponse<String> registerStaff(@RequestBody UserRequest userRequest) {
        userService.registerStaff(userRequest);
        return CommonResponse.<String>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data("Successfully registered").build();
    }

    @Operation(summary = "sign-in", description = "This method is used to sign-in. Needs email, password", tags = {"user-controller"})
    @PostMapping("/signin")
    public CommonResponse<TokenResponse> signIn(@RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = userService.signIn(signInRequest);
        return CommonResponse.<TokenResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data(tokenResponse).build();
    }

    @Operation(summary = "refresh-token", description = "This method is used to refresh token. Needs refresh-token", tags = {"user-controller"})
    @PostMapping("/refreshToken")
    public CommonResponse<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenResponse tokenResponse = userService.refreshToken(tokenRefreshRequest);
        return CommonResponse.<TokenResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully created").data(tokenResponse).build();
    }

    @Operation(summary = "get customer by emailId", description = "This method is used to get customer  by emailId", tags = {"user-controller"})
    @GetMapping(value = "/{email}")
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<UserResponse> getCustomer(@PathVariable(value = "email") String email) {
        UserResponse customerResponse = userService.getUserByEmail(email);
        return CommonResponse.<UserResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully fetched").data(customerResponse).build();
    }

    @Operation(summary = "get customer by emailId", description = "This method is used to get customer  by emailId", tags = {"user-controller"})
    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<UserResponse> getCustomerByToken() {
        UserResponse customerResponse = userService.getUserByToken(request);
        return CommonResponse.<UserResponse>builder().code(HttpStatus.CREATED.value()).message("Successfully fetched").data(customerResponse).build();
    }

    @Operation(summary = "update customer", description = "This method is used to update customer details  by Id", tags = {"user-controller"})
    @PutMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<String> updateUser( @RequestBody UpdateUserRequest updateRequest) {
        userService.updateUser(updateRequest, request);
        return CommonResponse.<String>builder().code(HttpStatus.CREATED.value()).message("Successfully updated").data("Successfully updated").build();
    }

    @Operation(summary = "update password", description = "This method is used to update customer password", tags = {"user-controller"})
    @PutMapping(path = "/updatePassword")
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatedPassword(updatePasswordRequest, request);
        return CommonResponse.<String>builder().code(HttpStatus.CREATED.value()).message("Successfully updated").data("Successfully updated").build();
    }

    @Operation(summary = "logout a customer.", description = "This method is used to logout a customer by Id", tags = {"user-controller"})
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public CommonResponse<String> logoutUser() {
        userService.logoutUser(request);
        return new CommonResponse<>(HttpStatus.OK.value(), "User logout Successfully.", "User logout Successfully.");

    }
}
