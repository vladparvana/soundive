package com.soundive.common.exception;

public class ValidationException extends BaseServiceException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}