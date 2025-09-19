package com.codementor.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    // 캐시 이름 정의
    public static final String POST_COUNT_CACHE = "postCount";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 캐시 이름 등록
        cacheManager.setCacheNames(java.util.List.of(POST_COUNT_CACHE));
        // Caffeine 설정
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // TTL 5분
                .maximumSize(1000);                  // 최대 1000개 항목
    }
}
