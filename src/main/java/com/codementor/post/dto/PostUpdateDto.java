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
public class PostUpdateDto {

    private Long id;
    private String title;
    private PostCategory category;
    private String content;
    private String isDeleted;
    private LocalDateTime updatedAt = LocalDateTime.now();

}
