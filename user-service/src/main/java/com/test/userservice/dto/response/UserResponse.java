package com.test.userservice.dto.response;

import com.test.userservice.enums.Gender;
import com.test.userservice.enums.UserType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private Gender gender;

    private LocalDate dob;

    private UserType userType;
}
