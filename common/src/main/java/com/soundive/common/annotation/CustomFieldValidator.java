package com.soundive.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator for the @ValidCustomField annotation.
 */
public class CustomFieldValidator implements ConstraintValidator<ValidCustomField, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Add your validation logic here
        return value != null && value.matches("[a-zA-Z]+");
    }
}