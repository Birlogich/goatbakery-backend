package org.example.validator;


import org.example.dto.CommentDto;
import org.example.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static void validateOrder(CommentDto comment) {
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(comment);

        if (!violations.isEmpty()) {

            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());


            throw new ValidationException(errorMessages);
        }
    }

}
