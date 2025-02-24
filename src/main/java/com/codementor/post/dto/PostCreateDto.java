package com.codementor.post.dto;

import com.codementor.post.enums.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private String title;
    private PostCategory category;
    private String content;
    private String authorName;

}
