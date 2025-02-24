package org.example.validator;

import org.example.dto.UserDto;
import org.example.exception.ValidationException;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();


    public static void validateUser(UserDto user) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        if (!violations.isEmpty()) {

            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());


            throw new ValidationException(errorMessages);
        }
    }
}
