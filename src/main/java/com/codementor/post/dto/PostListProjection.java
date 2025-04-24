package com.codementor.post.dto;

import java.time.LocalDateTime;

public interface PostListProjection {
    Long getId();
    String getTitle();
    String getAuthorNickname();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
