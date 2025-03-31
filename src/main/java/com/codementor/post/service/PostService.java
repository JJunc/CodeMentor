package com.codementor.post.service;

import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.*;
import com.codementor.post.dto.mapper.PostCreateMapper;
import com.codementor.post.dto.mapper.PostDetailMapper;
import com.codementor.post.dto.mapper.PostListMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.entity.PostImage;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostImageRepository;
import com.codementor.post.repository.PostRepository;

import com.codementor.utils.file.Base64Utils;
import com.codementor.utils.file.UploadFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
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

    public List<PostListDto> getPostList(PostCategory category) {
        return postRepository.findByCategoryAndNotDeleted(category).stream().map(postListMapper::toDto).collect(Collectors.toList());
    }

    public void createPost(PostCreateDto dto) {
        Optional<Member> findMember = memberRepository.findByUsername(dto.getAuthor());

        log.info("작성 글 내용: {}", dto.getContent());

        if (findMember.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Member author = findMember.get();  // 값이 있을 때만 꺼냄
        log.info("글작성 회원 아이디: {}", author.getUsername());
        Post post = postCreateMapper.toEntity(dto);
        post.setContent(Jsoup.parse(dto.getContent()).text());
        post.setAuthor(author);
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
        Post post = postRepository.findById(dto.getId()).orElse(null);
        post.update(dto);
        post.setContent(Jsoup.parse(dto.getContent()).text());
    }

    @Transactional
    public void deletePost(PostUpdateDto dto) {
        Post post = postRepository.findById(dto.getId()).orElse(null);
        post.setIsDeleted("Y");
    }

}
