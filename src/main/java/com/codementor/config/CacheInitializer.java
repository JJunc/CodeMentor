package com.codementor.config;

import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostCountCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheInitializer implements ApplicationRunner {

    private final PostCountCacheService postCountCacheService;

    @Override
    public void run(ApplicationArguments args) {
        for (PostCategory category : PostCategory.values()) {
            postCountCacheService.getPostCount(category); // 프록시 호출
        }
    }
}
