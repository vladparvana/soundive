package com.soundive.auth_service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * CacheConfig is a configuration class that defines caching behavior for the application
 * using the Caffeine cache library.
 *
 * This configuration sets up a Caffeine-based {@link CacheManager} with predefined properties.
 * The cache entries are configured to expire after 10 minutes of write operations
 * and the cache can hold a maximum of 500 entries.
 *
 * Declares a Spring-managed {@link CacheManager} bean to be used for caching purposes
 * throughout the application.
 */
@Configuration
public class CacheConfig {

    /**
     * Provides a {@link CacheManager} bean configured with the Caffeine cache library.
     * The cache is set to expire entries 10 minutes after they are written.
     * Additionally, the maximum number of entries allowed in the cache is 500.
     *
     * @return a {@link CacheManager} instance configured with Caffeine's cache settings.
     */
    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
