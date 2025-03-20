package com.codementor.reply.service;

import com.codementor.comment.repository.CommentRepository;
import com.codementor.reply.dto.ReplyDto;
import com.codementor.reply.dto.mapper.ReplyMapper;
import com.codementor.comment.entity.Comment;
import com.codementor.reply.entity.Reply;
import com.codementor.reply.repository.ReplyRepository;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.entity.Post;
import com.codementor.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ReplyMapper replyMapper;

    public void create(ReplyDto dto) {
        Reply reply = replyMapper.toEntity(dto);

        Comment parentComment = commentRepository.findById(dto.getParentId()).orElse(null);

        Post post = postRepository.findById(dto.getPostId()).orElse(null);

        Member member = memberRepository.findByUsername(dto.getAuthor()).orElse(null);
        log.info("답글 작성회원 : {}", dto.getAuthor());
        member.setUsername(dto.getAuthor());

        reply.setParent(parentComment);
        reply.setPost(post);
        reply.setMember(member);

        replyRepository.save(reply);
    }
}
