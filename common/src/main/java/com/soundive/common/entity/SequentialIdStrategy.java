package com.soundive.common.entity;

import java.util.concurrent.atomic.AtomicLong;

public class SequentialIdStrategy implements BaseIdStrategy {
    private final AtomicLong counter = new AtomicLong(1); // Could be replaced with DB or Redis-backed sequence

    @Override
    public String generateId() {
        return String.valueOf(counter.getAndIncrement());
    }
}