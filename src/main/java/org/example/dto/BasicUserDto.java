package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Value
@Builder
@Getter
public class BasicUserDto {

    @Min(value = 1, message = "ID must be above zero")
    int id;

    @NotEmpty(message = "First name cannot be empty")
    String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    String lastName;

    @NotEmpty(message = "Email cannot be empty")
    String email;

    @NotEmpty(message = "Phone name cannot be empty")
    String phoneNumber;
}
