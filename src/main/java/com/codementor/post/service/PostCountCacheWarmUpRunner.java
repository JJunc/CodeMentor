package com.codementor.post.service;

import com.codementor.post.enums.PostCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCountCacheWarmUpRunner implements ApplicationRunner {

    private final PostCountCacheService postCountCacheService;

    @Override
    public void run(ApplicationArguments args) {
        for (PostCategory category : PostCategory.values()) {
            postCountCacheService.getCountByCategoryCached(category); // 프록시를 통해 캐싱 적용됨
        }
    }
}
