package com.codementor.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private Long parentId;
    private Long id;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime updatedAt=LocalDateTime.now();

}
