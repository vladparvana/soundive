package com.soundive.common.exception;

public class DataPersistenceException extends BaseServiceException {
    public DataPersistenceException(String message) {
        super(message, "DATA_PERSISTENCE_ERROR");
    }
}