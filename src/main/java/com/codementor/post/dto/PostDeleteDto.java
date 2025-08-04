package com.codementor.post.dto;

import com.codementor.post.enums.PostCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDeleteDto {

    private long id;
    private String authorUsername;
    private PostCategory category;
}
