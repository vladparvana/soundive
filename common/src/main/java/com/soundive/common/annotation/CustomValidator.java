package com.soundive.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomValidator implements ConstraintValidator<ValidExample, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Custom logic validation logic
        return value != null && !value.isEmpty() && value.startsWith("valid");
    }
}