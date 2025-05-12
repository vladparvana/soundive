package com.soundive.user_service.exception;

import com.soundive.common.exception.BaseServiceException;
import com.soundive.common.exception.EntityNotFoundException;

public class UserNotFoundException extends BaseServiceException {
    public UserNotFoundException(String message ) {
        super(message,"USER_NOT_FOUND");
    }
}