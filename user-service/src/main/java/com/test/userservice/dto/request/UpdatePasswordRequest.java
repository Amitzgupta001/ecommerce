package com.test.userservice.dto.request;


import com.test.userservice.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;

}
