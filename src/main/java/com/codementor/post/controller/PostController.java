package com.codementor.post.controller;

import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.comment.service.CommentService;
import com.codementor.post.dto.*;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.ImageService;
import com.codementor.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final CommentService commentService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/{category}")
    public String posts(@PathVariable PostCategory category
            , @PageableDefault(page = 0, size = 10) Pageable pageable
            , Model model) {
        long start = System.currentTimeMillis();
        pageable = PageRequest.of(pageable.getPageNumber(), 10);
        if (category == null) {
            return "redirect:/post/FREE";
        }

        log.info("게시판 카테고리 = {}", category);
        log.info("페이지 넘버 = {}", pageable.getPageNumber());
        model.addAttribute("category", category);
        model.addAttribute("posts", postService.getPostsByCategory(category, pageable));
        long end = System.currentTimeMillis();

        log.info("실행시간 = {}", (end - start));
        return "/post/posts";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute PostSearchDto searchDto
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model, RedirectAttributes redirectAttributes) {
        // 회원 검색 결과
        Page<PostListDto> result = postService.searchPosts(searchDto, pageable);

        log.info("검색 카테고리 = {}", searchDto.getCategory());
        log.info("검색 타입 = {}", searchDto.getSearchType());

        if (result.isEmpty()) {
            log.info("검색 결과 존재하지 않음");
            redirectAttributes.addFlashAttribute("noResults", true); // 검색 결과 없음 플래그 추가
            return "redirect:/post/" + searchDto.getCategory(); // 다시 목록 페이지로 이동
        }

        model.addAttribute("category", searchDto.getCategory());
        model.addAttribute("posts", result);
        return "/post/posts";
    }


    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("dto", new PostCreateDto());
        return "/post/post-create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("dto") PostCreateDto dto,
                             HttpSession session,
                             Model model) throws IOException {

        postService.createPost(dto);

        return "redirect:/post/free";
    }

    @PostMapping("/api/images/upload")
    @ResponseBody
    public ResponseEntity uploadImage(@RequestParam MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지 파일이 비어있습니다.");
        }

        Map<String, String> result = imageService.saveImage(imageFile);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/images/delete-multiple")
    @ResponseBody
    public ResponseEntity deleteImages(@RequestBody Map<String, List<String>> request) {
        List<String> filenames = request.get("filenames");
        if (filenames != null) {
            log.info("이미지 삭제 실행");
            for (String filename : filenames) {
                imageService.deleteImageFile(filename);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category}/{id}")
    public String post(@PathVariable PostCategory category, @PathVariable Long id, Model model) {
        PostDetailDto post = postService.getPost(id);

        if (post == null) {
            model.addAttribute("post", id);
            return "/post/posts";
        }

        // 조회수 증가
        postService.updateViews(post);

        model.addAttribute("post", post);
        model.addAttribute("commentDto", new CommentResponseDto());

        return "/post/post-detail";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, HttpSession session, Model model) {

        PostDetailDto dto = postService.getPost(id);

        model.addAttribute("dto", dto);

        return "/post/post-edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@ModelAttribute PostUpdateDto dto, HttpSession session, RedirectAttributes redirectAttributes) {

        postService.updatePost(dto);

        redirectAttributes.addAttribute("category", dto.getCategory());
        redirectAttributes.addAttribute("id", dto.getId());

        return "redirect:/post/{category}/{id}";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@ModelAttribute PostUpdateDto dto, RedirectAttributes redirectAttributes) {
        postService.deletePost(dto);

        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/post/{category}";
    }
}
