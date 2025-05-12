package com.soundive.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to validate example conditions.
 */
@Constraint(validatedBy = CustomFieldValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomField {

    String message() default "Invalid value for the field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}