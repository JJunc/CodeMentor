package com.codementor.post.enums;

import lombok.Getter;

@Getter
public enum PostSearchType {
    TITLE,   // 제목 검색
    CONTENT, // 내용 검색
    AUTHOR,   // 작성자 검색
    TITLE_AND_CONTENT;
}
