package com.codementor.post.service;

import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.dto.mapper.PostCreateMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostCreateMapper postCreateMapper;

    public Page<Post> postList(PostCategory category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable);
    }

    public void createPost(PostCreateDto dto) {
        Optional<Member> findMember = memberRepository.findByUsername(dto.getAuthorName());

        log.info("작성 글 내용: {}", dto.getContent());

        if (findMember.isPresent()) {
            Member author = findMember.get();  // 값이 있을 때만 꺼냄
            log.info("글작성 회원 아이디: {}", author.getUsername());
            Post post = postCreateMapper.toEntity(dto);
            post.setAuthor(author);
            postRepository.save(post);
        } else {
            // Optional이 비어 있을 때 처리할 로직
            log.error("해당 아이디를 가진 회원이 존재하지 않습니다.");
            // 예외 처리나 다른 로직 추가
        }
    }

}
