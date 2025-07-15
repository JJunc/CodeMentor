package com.codementor.post.dto;

import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.post.enums.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private String authorNickname;
    private List<CommentResponseDto> comments;
    private PostCategory category;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
