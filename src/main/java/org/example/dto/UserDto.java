package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Builder
@Setter
@Getter
public class UserDto {
    @NotEmpty(message = "First name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    String lastName;

    @NotEmpty(message = "Address name cannot be empty")
    String address;

    @NotEmpty(message = "Email name cannot be empty")
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    String email;

    @NotEmpty(message = "Password name cannot be empty")
    String password;

    @NotEmpty(message = "Phone Number name cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    String phoneNumber;

}
