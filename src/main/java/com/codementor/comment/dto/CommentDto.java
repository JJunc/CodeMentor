package com.codementor.comment.dto;

import com.codementor.comment.entity.Comment;
import com.codementor.post.dto.PostDetailDto;
import com.codementor.reply.dto.ReplyDto;
import com.codementor.reply.entity.Reply;
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
    private Long parentId = null;
    private Long postId;
    private String author;
    private PostDetailDto postDetail;
    private String content;
    private List<ReplyDto> replies = new ArrayList<>();
    private int replyCount;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

}
