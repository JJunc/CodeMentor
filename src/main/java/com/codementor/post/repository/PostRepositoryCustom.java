package com.codementor.post.repository;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListDto> findByCategory(PostCategory category, int limit, int offset);
    List<PostListDto> findByTitleAndCategory(String keyword, PostCategory category, int limit, int offset);
    Long postCountByTitleAndCategory(String keyword, PostCategory category);
}
