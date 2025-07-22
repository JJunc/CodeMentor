package com.codementor.post.service;

import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCountCacheService {
    private final PostRepository postRepository;

    @Cacheable(value = "postCountByCategory", key = "#category")
    public Long getCountByCategoryCached(PostCategory category) {

        return postRepository.countByCategory(category);
    }


}
