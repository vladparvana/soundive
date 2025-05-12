package com.soundive.common.exception;

public class EntityNotFoundException extends BaseServiceException {
    public EntityNotFoundException(String message) {
        super(message, "ENTITY_NOT_FOUND");
    }
}