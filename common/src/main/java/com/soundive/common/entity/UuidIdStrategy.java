package com.soundive.common.entity;

import java.util.UUID;

public class UuidIdStrategy implements BaseIdStrategy {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}