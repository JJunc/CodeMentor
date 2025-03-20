package com.codementor.post.dto;

import com.codementor.post.enums.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private Long id;
    private String title;
    private PostCategory category;
    private Long postId;
    private String content;
    private String author;
    private List<String> tempImageIds; // 클라이언트에서 받은 임시 이미지 ID
    private List<String> imagePaths; // 최종 저장된 이미지 경로

}
