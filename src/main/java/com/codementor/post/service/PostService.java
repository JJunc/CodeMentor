package com.codementor.post.service;


import com.codementor.config.CacheConfig;
import com.codementor.exception.PostNotFoundException;
import com.codementor.member.entity.Member;
import com.codementor.exception.MemberNotFoundException;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.*;
import com.codementor.post.dto.mapper.PostCreateMapper;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.dto.mapper.PostListMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;

import com.codementor.post.repository.PostRepositoryImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    private final MemberRepository memberRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostDetailMapper postDetailMapper;
    private final PostCountCacheService postCountCacheService;
    private final PostListMapper postListMapper;
    private final CacheManager cacheManager;

    public Page<PostListDto> getPostsByCategory(PostCategory category, Pageable pageable) {
        Long totalCount = postCountCacheService.getPostCount(category);
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        log.info("category= {} ", category);
        List<PostListDto> posts = postRepositoryImpl.findByCategory(category, limit, offset);
        return new PageImpl<>(posts, pageable, totalCount);
    }

    public Page<PostListDto> searchPosts(PostSearchDto dto, Pageable pageable) {
        List<PostListDto> posts = null;
        log.info("검색조건: {}, 카테고리: {}, 검색 키워드: {}" , dto.getSearchType(), dto.getCategory(), dto.getKeyword());
        Long totalCount = postRepositoryImpl.postCountByTitleAndCategory(dto.getKeyword(), dto.getCategory());
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

            switch (dto.getSearchType()) {
                case TITLE:
                    log.info("제목 검색 실행");
                    posts = postRepositoryImpl.findByTitleAndCategory(dto.getKeyword(), dto.getCategory(), limit, offset);
//                    posts = postRepository.findByTitleAndCategory(dto.getKeyword(), dto.getCategory(), limit, offset);
                    break;
//                case CONTENT:
//                    posts = postRepository.findByContentAndCategory(dto.getKeyword(), dto.getCategory(), pageable);
//                    break;
//                case TITLE_AND_CONTENT:
//                    posts = postRepository.findByTitleOrContentAndCategory(dto.getKeyword(), dto.getKeyword(), dto.getCategory(), pageable);
//                    break;
//                case AUTHOR:
//                    posts = postRepository.findByAuthorAndCategory(dto.getKeyword(), dto.getCategory(), pageable);
//                    break;
            }

        return new PageImpl<>(posts, pageable, totalCount);
    }

//    public Page<PostListDto> searchPosts(PostSearchDto searchDto, Pageable pageable) {
//        return postRepository.searchByTitleAndCategory(searchDto.getKeyword(), searchDto.getCategory(), pageable);
//    }

    @Transactional
    public void createPost(PostCreateDto dto) {
        Member findMember = memberRepository.findByUsername
                (dto.getAuthorUsername()).orElseThrow(() ->  new MemberNotFoundException("존재하지 않는 회원입니다."));
        dto.setAuthorNickname(findMember.getNickname());

        log.info("작성 글 내용: {}", dto.getContent());


        log.info("글작성 회원 아이디: {}", findMember.getUsername());
        log.info("글작성 회원 아이디: {}", findMember.getNickname());

        Post post = postCreateMapper.toEntity(dto);
        post.createPost(dto);
        postRepository.save(post);

        postCountCacheService.incrementPostCount(dto.getCategory());
        printCache(dto.getCategory());
    }

    public void printCache(PostCategory category) {
        Long cachedCount = postCountCacheService.getPostCount(category);

        log.info("printCache | category={} | cachedCount={}", category, cachedCount);
    }

    @Transactional
    public void updateViews(PostDetailDto dto) {
        Post post = postRepository.findById(dto.getId()).orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        postRepository.incrementViewCount(dto.getId());
    }


    public PostDetailDto getPost(Long id) {
        return postDetailMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new PostNotFoundException("해당 게시글이 존재하지 않습니다.")));
    }

    @Transactional
    public void updatePost(PostUpdateDto dto) {
        Post post = getPost(dto);
        post.update(dto);
    }


    @Transactional
    public void deletePost(PostDeleteDto dto) {
        Post post = getPost(dto);
        post.delete(dto);

        postCountCacheService.decrementPostCount(dto.getCategory());
    }

    // 게시글 찾기 메소드
    private Post getPost(PostDeleteDto dto) {
        Post post = postRepository.findById(dto.getId()).orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        Member author = memberRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new AccessDeniedException("작성자 정보를 확인할 수 없습니다."));

        if (!post.getAuthorUsername().equals(author.getUsername())) {
            throw new AccessDeniedException("작성자가 아닙니다.");
        }

        return post;
    }

    private Post getPost(PostUpdateDto dto) {
        Post post = postRepository.findById(dto.getId()).orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        Member author = memberRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new AccessDeniedException("작성자 정보를 확인할 수 없습니다."));

        if (!post.getAuthorUsername().equals(author.getUsername())) {
            throw new AccessDeniedException("작성자가 아닙니다.");
        }

        return post;
    }

}
