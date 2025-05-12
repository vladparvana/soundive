package com.soundive.user_service.exception;

import com.soundive.common.exception.BaseServiceException;

public class UserAlreadyExistsException extends BaseServiceException {
    public UserAlreadyExistsException(String message) {
        super(message,"USER_ALREADY_EXISTS");
    }
}