package com.codementor.post.dto;

import com.codementor.post.enums.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {

    private Long id;
    private String title;
    private String authorName;
    private PostCategory category;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
