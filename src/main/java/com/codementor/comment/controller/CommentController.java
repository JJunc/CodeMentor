package com.codementor.comment.controller;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.service.CommentService;
import com.codementor.intetceptor.AuthorOnly;
import com.codementor.intetceptor.AuthorType;
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
        Page<CommentDto> commentDtoList = commentService.getComments(postId, pageable);

        log.info("총 댓글 갯수 = {}", commentDtoList.getTotalElements());
        log.info("게시판 번호 = {}", postId);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity saveComment(@RequestBody CommentDto commentDto,
                                     @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("댓글이 작성될 게시판 번호 = {}", commentDto.getPostId());
        Long saveResult = commentService.create(commentDto);
        if (saveResult != null) {
            Page<CommentDto> commentDtoList = commentService.getComments(commentDto.getPostId(), pageable);
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reply/save")
    public ResponseEntity replyComment(
                                       @RequestBody CommentDto commentDto,
                                       @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("답글이 작성될 게시판 번호 = {}", commentDto.getPostId());
        Long saveResult = commentService.saveReply(commentDto);
        if (saveResult != null) {
            Page<CommentDto> commentDtoList = commentService.getComments(commentDto.getPostId(), pageable);
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @AuthorOnly(type = AuthorType.COMMENT)
    @PostMapping("/edit")
    public ResponseEntity editComment(@RequestBody CommentDto commentDto,
                                      @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        commentService.edit(commentDto);
        Page<CommentDto> commentDtoList = commentService.getComments(commentDto.getPostId(), pageable);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @AuthorOnly(type = AuthorType.COMMENT)
    @PostMapping("/delete")
    public ResponseEntity deleteComment(@RequestBody CommentDto commentDto,
                                        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        commentService.delete(commentDto);
        Page<CommentDto> commentDtoList = commentService.getComments(commentDto.getPostId(), pageable);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

}
