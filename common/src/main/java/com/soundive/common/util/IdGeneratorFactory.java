package com.soundive.common.util;

import com.soundive.common.entity.BaseIdStrategy;
import com.soundive.common.entity.SequentialIdStrategy;
import com.soundive.common.entity.UuidIdStrategy;

public class IdGeneratorFactory {
    public static BaseIdStrategy getIdStrategy(String strategyType) {
        switch (strategyType.toUpperCase()) {
            case "UUID":
                return new UuidIdStrategy();
            case "SEQUENTIAL":
                return new SequentialIdStrategy();
            default:
                throw new IllegalArgumentException("Unknown ID Generation Strategy: " + strategyType);
        }
    }
}