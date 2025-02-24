package org.example.validator;

import org.example.dto.OrderDto;
import org.example.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();


    public static void validateOrder(OrderDto order) {
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(order);

        if (!violations.isEmpty()) {

            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());


            throw new ValidationException(errorMessages);
        }
    }
}
