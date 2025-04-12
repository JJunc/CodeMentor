package com.codementor.intetceptor;

import com.codementor.comment.entity.Comment;
import com.codementor.comment.repository.CommentRepository;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.entity.Member;
import com.codementor.post.entity.Post;
import com.codementor.post.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorCheckAspect {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Around("@annotation(authorOnly)")
    public Object checkAuthor(ProceedingJoinPoint joinPoint, AuthorOnly authorOnly) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long targetId = null;
        HttpSession session = null;

        for (Object arg : args) {
            if (arg instanceof Long) targetId = (Long) arg;
            if (arg instanceof HttpSession) session = (HttpSession) arg;
        }

        if (targetId == null || session == null) {
            throw new IllegalArgumentException("id 또는 session이 필요합니다.");
        }

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute("loginMember");


        if (loginMember == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        boolean isAuthor = switch (authorOnly.type()) {
            case POST -> {
                Post post = postRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
                yield post.getAuthorUsername().equals(loginMember.getUsername());
            }
            case COMMENT -> {
                Comment comment = commentRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));
                yield comment.getMember().getUsername().equals(loginMember.getUsername());
            }
        };

        if (!isAuthor) {
            throw new AccessDeniedException("작성자만 접근 가능합니다.");
        }

        return joinPoint.proceed(); // 권한 통과 시 실행
    }
}
