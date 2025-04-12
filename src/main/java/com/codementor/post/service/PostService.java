package com.codementor.post.service;


import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.*;
import com.codementor.post.dto.mapper.PostCreateMapper;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.dto.mapper.PostListMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostDetailMapper postDetailMapper;
    private final PostListMapper postListMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Page<PostListDto> getPostList(PostCategory category, Pageable pageable) {
        return postRepository.findByCategoryAndNotDeleted(category, pageable)
                .map(postListMapper::toDto);
    }

    public Page<PostListDto> searchPosts(PostSearchDto dto, Pageable pageable) {
        Page<Post> posts;
        log.info("검색조건: {}, 카테고리: {}", dto.getSearchType(), dto.getCategory());

        // 검색 조건이 없는 경우 전체 조회
        if (dto.getSearchType() == null || dto.getKeyword() == null || dto.getKeyword().isEmpty()) {
            posts = postRepository.findByCategory(dto.getCategory(), pageable);
        } else {
            // SearchType에 따라 조건을 다르게 처리
            switch (dto.getSearchType()) {
                case TITLE:
                    log.info("제목 검색 실행");
                    posts = postRepository.findByTitleAndCategory(dto.getKeyword(), dto.getCategory(), pageable);
                    break;
                case CONTENT:
                    posts = postRepository.findByContentAndCategory(dto.getKeyword(), dto.getCategory(), pageable);
                    break;
                case TITLE_AND_CONTENT:
                    posts = postRepository.findByTitleOrContentAndCategory(dto.getKeyword(), dto.getKeyword(), dto.getCategory(), pageable);
                    break;
                case AUTHOR:
                    posts = postRepository.findByAuthorAndCategory(dto.getKeyword(), dto.getCategory(), pageable);
                    break;
                default:
                    posts = postRepository.findAll(pageable);  // 기본적으로 전체 검색
                    break;
            }
        }

        return posts.map(postListMapper::toDto);
    }

    @Transactional
    public void createPost(PostCreateDto dto) {
        Member findMember = memberRepository.findByUsername
                (dto.getAuthor()).orElseThrow(() ->  new IllegalStateException("존재하지 않는 회원입니다."));
        dto.setAuthorNickname(findMember.getNickname());

        log.info("작성 글 내용: {}", dto.getContent());


        log.info("글작성 회원 아이디: {}", findMember.getUsername());
        log.info("글작성 회원 아이디: {}", findMember.getNickname());

        Post post = postCreateMapper.toEntity(dto);
        post.createPost(dto);
        postRepository.save(post);
    }



    @Transactional
    public boolean savePost(PostCreateDto dto) throws IOException {
        // 게시글 저장
        Post post = new Post();
        postRepository.save(post);

        if(dto.getImagePaths() != null) {

        }

        return true;
    }

    @Transactional
    public void updateViews(PostDetailDto dto) {
        Post post = postRepository.findById(dto.getId()).orElse(null);
        post.increaseViews();
    }


    public PostDetailDto getPost(Long id) {
        return postDetailMapper.toDto(postRepository.findById(id).orElse(null));
    }

    @Transactional
    public void updatePost(PostUpdateDto dto) {
        Post post = postRepository.findById(dto.getId()).orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));
        post.update(dto);
    }

    @Transactional
    public void deletePost(PostUpdateDto dto) {
        Post post = postRepository.findById(dto.getId()).orElse(null);
        post.setIsDeleted("Y");
    }

}
