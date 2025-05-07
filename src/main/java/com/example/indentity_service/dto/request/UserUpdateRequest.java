package com.example.indentity_service.dto.request;

import com.example.indentity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
    @Size(min = 8,message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min = 18,message = "INVALID_DOB")
     LocalDate dob;
     List<String> roles;

}
