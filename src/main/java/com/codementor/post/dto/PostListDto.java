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
    private String authorUsername;
    private String authorNickname;
    private PostCategory category;
    private String deleted;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostListDto(Long id, String title, String authorUsername, String authorNickname, int views
            ,PostCategory category, LocalDateTime createdAt, LocalDateTime updatedAt, String deleted) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
        this.authorNickname = authorNickname;
        this.category = category;
        this.deleted = deleted;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
