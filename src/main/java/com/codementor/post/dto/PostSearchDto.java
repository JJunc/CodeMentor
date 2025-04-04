package com.codementor.post.dto;

import com.codementor.post.enums.PostCategory;
import com.codementor.post.enums.PostSearchType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostSearchDto {
    private String keyword; // 검색어
    private PostSearchType searchType; // 검색 기준 (제목, 내용, 작성자 등)
    private PostCategory category;
    private LocalDateTime startDate; // 시작일
    private LocalDateTime endDate; // 종료일
}
