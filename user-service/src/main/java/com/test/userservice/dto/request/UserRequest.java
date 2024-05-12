package com.test.userservice.dto.request;


import com.test.userservice.enums.Gender;
import com.test.userservice.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@Builder
public class UserRequest {

    @NotNull
    @Schema(required = true)
    private String firstName;

    @NotNull
    @Schema(required = true)
    private String lastName;

    @NotNull
    @Schema(required = true)
    private String email;

    @NotNull
    @Schema(required = true)
    private String mobileNumber;

    private String password;

    @NotNull
    @Schema(required = true)
    private Gender gender;

    @NotNull
    @Schema(required = true)
    private LocalDate dob;

    @NotNull
    private UserType userType;

}
