package com.codementor.post.controller;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.service.CommentService;
import com.codementor.intetceptor.AuthorOnly;
import com.codementor.intetceptor.AuthorType;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.MemberListDto;
import com.codementor.member.enums.SessionConst;
import com.codementor.post.dto.*;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${temp.upload.dir}")
    private String tempUploadDir;

    @GetMapping("/{category}")
    public String posts(@PathVariable PostCategory category
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        if (category == null) {
            return "redirect:/post/FREE";
        }

        model.addAttribute("category", category);
        model.addAttribute("posts", postService.getPostList(category, pageable));
        return "/post/posts";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute PostSearchDto searchDto
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model, RedirectAttributes redirectAttributes) {
        // 회원 검색 결과
        Page<PostListDto> result = postService.searchPosts(searchDto, pageable);

        log.info("검색 카테고리 = {}", searchDto.getCategory());

        if (result.isEmpty()) {
            model.addAttribute("noResults", true); // 검색 결과 없음 플래그 추가
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
        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        dto.setAuthor(loginMember.getUsername());

        postService.createPost(dto);

        return "redirect:/post/free";
    }

    @PostMapping("/api/images/upload")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지 파일이 비어있습니다.");
        }

        try {
            // 예시: 서버에 저장
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path savePath = Paths.get(uploadDir, fileName);
            Files.copy(imageFile.getInputStream(), savePath);

            // 이미지 접근 URL 생성 (정적 자원으로 서빙하거나 직접 서빙)
            String imageUrl = "/images/" + fileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", imageUrl);
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    @PostMapping("/upload-image")
    @ResponseBody
    public Map<String, Object> uploadImage2(@RequestParam("upload") MultipartFile file) throws IOException {
        // 저장 디렉토리
        String uploadDir = "/Users/yourname/upload/images"; // 실제 경로

        // 고유 파일 이름 생성
        String uuid = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadDir, uuid);
        Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        // 응답
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/images/" + uuid); // 중요! 절대 경로로 주기
        return response;
    }


    @GetMapping("/{category}/{id}")
    public String post(@PathVariable PostCategory category, @PathVariable Long id, Model model) {
        PostDetailDto post = postService.getPost(id);

        log.info("조회되는 게시판 번호 = {}", id);
        log.info("조회된 게시판의 번호 필드= {}", post.getId());
        log.info("게시글 내용: {}", post.getContent());

        if (post == null) {
            model.addAttribute("post", id);
            return "/post/posts";
        }

        // 조회수 증가
        postService.updateViews(post);

        log.info("댓글 유무 = {}", post.getComments() == null ? "null" : post.getComments().isEmpty());

        model.addAttribute("post", post);
        model.addAttribute("commentDto", new CommentDto());

        return "/post/post-detail";
    }

    @AuthorOnly(type = AuthorType.POST)
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, HttpSession session, Model model) {
        PostDetailDto dto = postService.getPost(id);
        model.addAttribute("dto", dto);

        return "/post/post-edit";
    }

    @AuthorOnly(type = AuthorType.POST)
    @PostMapping("/edit/{id}")
    public String updatePost(@ModelAttribute PostUpdateDto dto, HttpSession session, RedirectAttributes redirectAttributes) {
        postService.updatePost(dto);

        log.info("수정된 게시글 제목 = {}", dto.getTitle());

        redirectAttributes.addAttribute("category", dto.getCategory());
        redirectAttributes.addAttribute("id", dto.getId());

        return "redirect:/post/{category}/{id}";
    }

    @AuthorOnly(type = AuthorType.POST)
    @PostMapping("/delete/{id}")
    public String deletePost(@ModelAttribute PostUpdateDto dto, RedirectAttributes redirectAttributes) {
        log.info("삭제된 게시글 제목 = {}", dto.getTitle());

        postService.deletePost(dto);

        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/post/{category}";
    }
}
