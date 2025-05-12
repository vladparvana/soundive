package com.soundive.auth_service.exception;

import com.soundive.common.exception.BaseServiceException;

public class TokenValidationException extends BaseServiceException {
    public TokenValidationException(String message) {
        super(message,"INVALID_TOKEN");
    }
}