package com.codementor.comment.service;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.dto.mapper.CommentMapper;
import com.codementor.comment.entity.Comment;
import com.codementor.comment.repository.CommentRepository;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.repository.PostRepository;
import com.codementor.reply.dto.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ReplyMapper replyMapper;
    private final PostDetailMapper postDetailMapper;

    public Long create(CommentDto commentDto) {
        // 댓글을 작성할 게시판과 회원 조회
        Optional<Post> optionalPost = postRepository.findById(commentDto.getPostId());
        Member author = memberRepository.findByUsername(commentDto.getAuthor()).get();
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            commentDto.setPostId(post.getId());

            Comment comment = commentMapper.toEntity(commentDto);
            comment.setPost(post);
            comment.setMember(author);
            return commentRepository.save(comment).getId();
        } else {
            return null;
        }
    }

    public Page<CommentDto> getComments(Long postId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);
        return commentPage.map(comment -> {
            CommentDto dto = commentMapper.toDto(comment);
            log.info("댓글이 작성된 날짜 = {}",dto.getCreatedAt());
            dto.setReplies(comment.getReplies().stream()
                    .map(replyMapper::toDto)
                    .collect(Collectors.toList()));
            return dto;
        });
    }

    public Page<CommentDto> getMyComments(String username, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByUsername(username, pageable);
        return commentPage.map(comment -> {
            CommentDto dto = commentMapper.toDto(comment);
            Post post = postRepository.findById(dto.getPostId()).get();
            log.info("댓글이 작성된 게시글 = {}",post.getTitle());
            log.info("댓글이 작성된 날짜 = {}",dto.getCreatedAt());
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

        comment.delete();
    }

}
