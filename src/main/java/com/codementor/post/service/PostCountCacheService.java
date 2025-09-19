package com.codementor.post.service;

import com.codementor.config.CacheConfig;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;
import groovy.util.logging.Slf4j;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class PostCountCacheService {

    private final PostRepository postRepository;
    private final CacheManager cacheManager;

    // 캐시에서 COUNT 조회, 없으면 DB 조회 후 캐시 저장
    @Cacheable(value = CacheConfig.POST_COUNT_CACHE, key = "#category")
    public Long getPostCount(PostCategory category) {
        return postRepository.countByCategoryAndIsDeletedFalse(category);
    }

    // 게시글 추가 시 캐시 증가
    @CachePut(value = CacheConfig.POST_COUNT_CACHE, key = "#category")
    public Long incrementPostCount(PostCategory category) {
        Long count = cacheManager.getCache(CacheConfig.POST_COUNT_CACHE)
                                 .get(category, Long.class);
        if (count == null) {
            count = getPostCount(category);
        }
        return count + 1;
    }

    // 게시글 삭제 시 캐시 감소
    @CachePut(value = CacheConfig.POST_COUNT_CACHE, key = "#category")
    public Long decrementPostCount(PostCategory category) {
        Long count = cacheManager.getCache(CacheConfig.POST_COUNT_CACHE)
                                 .get(category, Long.class);
        if (count == null) {
            count = getPostCount(category);
        }
        return count - 1;
    }



}
