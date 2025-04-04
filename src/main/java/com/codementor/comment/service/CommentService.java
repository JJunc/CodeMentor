package com.codementor.comment.service;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.dto.CommentSearchDto;
import com.codementor.comment.dto.mapper.CommentMapper;
import com.codementor.comment.entity.Comment;
import com.codementor.comment.repository.CommentRepository;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostDetailMapper postDetailMapper;

    public Long create(CommentDto commentDto) {
        // 댓글을 작성할 게시판과 회원 조회
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Member author = memberRepository.findByUsername(commentDto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        log.info("댓글 작성 닉네임: {}", author.getNickname());

        // DTO → 엔티티 변환
        commentDto.setNickname(author.getNickname());
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(post);
        comment.setMember(author);

        return commentRepository.save(comment).getId();
    }


    public Long saveReply(CommentDto commentDto) {
        // 게시글 조회 (없으면 예외 발생)
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 조회 (없으면 예외 발생)
        Member author = memberRepository.findByUsername(commentDto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // DTO → 엔티티 변환
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(post);
        comment.setMember(author);

        // 부모 댓글이 존재하면 설정
        if (commentDto.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            comment.setParent(parentComment);
            comment.setDepth(1);
        }

        return commentRepository.save(comment).getId();
    }


    public Page<CommentDto> searchComments(CommentSearchDto dto, Pageable pageable) {
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

        return comments.map(commentMapper::toDto);
    }

    public Page<CommentDto> getComments(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return comments.map(commentMapper::toDto); // Mapper 사용
    }


    public Page<CommentDto> getComments(Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        return commentPage.map(comment -> {
            Post post = postRepository.findById(comment.getPost().getId()).get();
            CommentDto dto = commentMapper.toDto(comment);
            dto.setPostDetail(postDetailMapper.toDto(post));
            log.info("댓글이 작성된 날짜 = {}", dto.getCreatedAt());
            return dto;
        });
    }

    public Page<CommentDto> getMyComments(String username, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByUsername(username, pageable);
        return commentPage.map(comment -> {
            CommentDto dto = commentMapper.toDto(comment);
            Post post = postRepository.findById(dto.getPostId()).get();
            log.info("댓글이 작성된 게시글 = {}", post.getTitle());
            log.info("댓글이 작성된 날짜 = {}", dto.getCreatedAt());
            dto.setPostDetail(postDetailMapper.toDto(post));
            return dto;
        });
    }

    @Transactional
    public void edit(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElse(null);

        comment.update(commentDto);
    }

    @Transactional
    public void delete(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElse(null);

        if (comment.getParent() != null) {
            comment.removeReply(comment);
        } else {
            comment.delete();
        }
    }

}
