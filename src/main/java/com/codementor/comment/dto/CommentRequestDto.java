package com.codementor.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private Long id;
    private Long postId;
    private Long parentId;
    private String authorUsername;
    private String content;

}
