package com.codementor.comment.dto;

import com.codementor.comment.enums.CommentSearchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSearchDto {

    private String keyword;
    private CommentSearchType searchType;

}
