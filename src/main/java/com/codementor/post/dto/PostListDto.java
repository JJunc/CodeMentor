package com.codementor.post.dto;

import com.codementor.post.entity.Post;
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
    private String authorNickname;
    private PostCategory category;
    private Boolean isDeleted;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostListDto(Long id, String title, String authorNickname, int views
            ,PostCategory category, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.authorNickname = authorNickname;
        this.category = category;
        this.isDeleted = isDeleted;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
