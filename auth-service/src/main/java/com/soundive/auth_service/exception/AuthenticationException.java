package com.soundive.auth_service.exception;

import com.soundive.common.exception.BaseServiceException;

public class AuthenticationException extends BaseServiceException {
    public AuthenticationException(String message) {
        super(message,"AUTHENTICATION_ERROR");
    }
}