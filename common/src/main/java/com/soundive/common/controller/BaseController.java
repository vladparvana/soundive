package com.soundive.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Provides common response utilities for controllers.
 */
public abstract class BaseController {

    protected <T> ResponseEntity<T> respondOk(T result) {
        return ResponseEntity.ok(result);
    }

    protected ResponseEntity<Void> respondNoContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected <T> ResponseEntity<T> respondCreated(T result) {
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}