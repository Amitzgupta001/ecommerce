package com.test.userservice.dto.request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
@Builder
public class SignInRequest {

    @Email
    private String email;

    @NotEmpty
    private String password;

}
