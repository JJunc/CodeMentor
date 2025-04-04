package com.codementor.comment.dto;

import com.codementor.comment.entity.Comment;
import com.codementor.post.dto.PostDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long parentId;
    private Long postId;
    private String author;
    private String nickname;
    private PostDetailDto postDetail;
    private String content;
    private String isDeleted = "N";
    private List<CommentDto> replies = new ArrayList<>();
    private int depth;
    private int replyCount;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

}
