package com.codementor.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long parentId = null;
    private Long postId;
    private String author;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
}
