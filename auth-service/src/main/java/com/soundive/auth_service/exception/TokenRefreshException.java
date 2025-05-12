package com.soundive.auth_service.exception;

import com.soundive.common.exception.BaseServiceException;

public class TokenRefreshException extends BaseServiceException {
    public TokenRefreshException(String message) {
        super(message, "REFRESH_TOKEN_ERROR");
    }
}
