package com.codementor.comment.service;

import com.codementor.comment.dto.CommentRequestDto;
import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.comment.dto.CommentSearchDto;
import com.codementor.comment.dto.mapper.CommentMapper;
import com.codementor.comment.entity.Comment;
import com.codementor.comment.repository.CommentRepository;
import com.codementor.exception.CommentNotFoundException;
import com.codementor.exception.MemberNotFoundException;
import com.codementor.exception.PostNotFoundException;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostDetailMapper postDetailMapper;

    public Long create(CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));


        log.info("댓글 작성 닉네임: {}", commentRequestDto.getAuthorUsername());

        Member author = memberRepository.findByUsername(commentRequestDto.getAuthorUsername())
                .orElseThrow(() -> new MemberNotFoundException("작성자를 찾을 수 없습니다."));

        Comment comment = commentMapper.requestToEntity(commentRequestDto);
        comment.setAuthorNickname(author.getNickname());

        return commentRepository.save(comment).getId();
    }


    public Long createReply(CommentRequestDto commentRequestDto) {
        // 게시글 조회 (없으면 예외 발생)
        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        // 작성자 조회 (없으면 예외 발생)
        Member author = memberRepository.findByUsername(commentRequestDto.getAuthorUsername())
                .orElseThrow(() -> new MemberNotFoundException("작성자를 찾을 수 없습니다."));

        // DTO → 엔티티 변환
        Comment comment = commentMapper.requestToEntity(commentRequestDto);
        comment.setAuthorNickname(author.getNickname());

        // 부모 댓글이 존재하면 설정
        if (commentRequestDto.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentRequestDto.getParentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글을 찾을 수 없습니다."));

            comment.setParent(parentComment);
            comment.setDepth(1);
        }

        return commentRepository.save(comment).getId();
    }


    public Page<CommentResponseDto> searchComments(CommentSearchDto dto, Pageable pageable) {
        Page<Comment> comments;

        // 검색 조건이 없는 경우 전체 조회
        if (dto.getSearchType() == null || dto.getKeyword() == null || dto.getKeyword().isEmpty()) {
            comments = commentRepository.findAll(pageable);
        } else {
            // SearchType에 따라 조건을 다르게 처리
            switch (dto.getSearchType()) {
                case TITLE:
                    comments = commentRepository.findByPostTitle(dto.getKeyword(), pageable);
                    break;
                case CONTENT:
                    comments = commentRepository.findByContent(dto.getKeyword(), pageable);
                    break;
                case AUTHOR:
                    comments = commentRepository.findByAuthorUsername(dto.getKeyword(), pageable);
                    break;
                default:
                    comments = commentRepository.findAll(pageable);  // 기본적으로 전체 검색
                    break;
            }
        }

        return comments.map(commentMapper::toResponseDto);
    }

    public Page<CommentResponseDto> getComments(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return comments.map(commentMapper::toResponseDto); // Mapper 사용
    }


    public Page<CommentResponseDto> getComments(CommentRequestDto requestDto, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        return commentPage.map(comment -> {
            Post post = postRepository.findById(requestDto.getPostId()).get();
            CommentResponseDto responseDto = commentMapper.toResponseDto(comment);
            responseDto.setPostDetail(postDetailMapper.toDto(post));
            return responseDto;
        });
    }

    public Page<CommentResponseDto> getMyComments(String username, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByUsername(username, pageable);
        return commentPage.map(comment -> {
            CommentResponseDto dto = commentMapper.toResponseDto(comment);
            Post post = postRepository.findById(dto.getPostId()).get();
            log.info("댓글이 작성된 게시글 = {}", post.getTitle());
            log.info("댓글이 작성된 날짜 = {}", dto.getCreatedAt());
            dto.setPostDetail(postDetailMapper.toDto(post));
            return dto;
        });
    }

    @Transactional
    public void edit(CommentRequestDto commentRequestDto) {
        Comment comment = getComment(commentRequestDto);

        comment.update(commentRequestDto);
    }

    @Transactional
    public void delete(CommentRequestDto commentRequestDto) {
        Comment comment = getComment(commentRequestDto);


        if (comment.getParent() != null) {
            comment.removeReply(comment);
        } else {
            comment.delete();
        }
    }

    // 댓글 찾기 메소드
    private Comment getComment(CommentRequestDto commentRequestDto) {
        Member member = memberRepository.findByUsername(commentRequestDto.getAuthorUsername()).orElseThrow(() ->
                new MemberNotFoundException("존재하지 않는 회원 입니다."));

        Comment comment = commentRepository.findById(commentRequestDto.getId()).orElseThrow(()
                -> new CommentNotFoundException("존재하지 않는 댓글입니다."));

        if(!comment.getAuthorUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("접근권한이 없습니다.");
        }
        return comment;
    }

}
