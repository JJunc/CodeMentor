package com.codementor.post.controller;

import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.SessionConst;
import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/{category}")
    public String posts(@PathVariable PostCategory category
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        if (category == null) {
            return "redirect:/post/FREE";
        }

        model.addAttribute("posts", postService.postList(category, pageable));
        return "/post/posts";
    }

    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("dto", new PostCreateDto());
        return "/post/post-create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("dto") PostCreateDto dto
                             , HttpSession session
                            , Model model) {
        LoginResponseDto loginMember = (LoginResponseDto)  session.getAttribute(SessionConst.LOGIN_MEMBER);
        dto.setAuthorName(loginMember.getUsername());
        log.info("글작성 회원 아이디: {}", dto.getAuthorName());
        log.info("작성 글 내용: {}", dto.getContent());
        postService.createPost(dto);
        return "redirect:/posts";
    }

}
