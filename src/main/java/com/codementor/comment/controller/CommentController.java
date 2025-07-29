package com.codementor.comment.controller;

import com.codementor.comment.dto.CommentRequestDto;
import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity getCommentList(@PathVariable Long postId,
                                         @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponseDto> commentDtoList = commentService.getComments(postId, pageable);

        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity createComment(@RequestBody CommentRequestDto commentRequestDto,
                                     @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("댓글이 작성될 게시판 번호 = {}", commentRequestDto.getPostId());
        Long saveResult = commentService.create(commentRequestDto);
        if (saveResult != null) {
            Page<CommentResponseDto> commentDtoList = commentService.getComments(commentRequestDto.getPostId(), pageable);
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reply/create")
    public ResponseEntity replyComment(
                                       @RequestBody CommentRequestDto commentRequestDto,
                                       @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("답글이 작성될 게시판 번호 = {}", commentRequestDto.getPostId());
        Long saveResult = commentService.createReply(commentRequestDto);
        if (saveResult != null) {
            Page<CommentResponseDto> commentDtoList = commentService.getComments(commentRequestDto.getPostId(), pageable);
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity editComment(@RequestBody CommentRequestDto commentRequestDto,
                                      @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        commentService.edit(commentRequestDto);
        Page<CommentResponseDto> commentDtoList = commentService.getComments(commentRequestDto.getPostId(), pageable);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteComment(@RequestBody CommentRequestDto commentRequestDto,
                                        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        commentService.delete(commentRequestDto);
        Page<CommentResponseDto> commentDtoList = commentService.getComments(commentRequestDto.getPostId(), pageable);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

}
