package com.codementor;

import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("freePosts", postService.getPostsByCategory(PostCategory.FREE, PageRequest.of(0, 5)));
        model.addAttribute("backendPosts", postService.getPostsByCategory(PostCategory.BACKEND, PageRequest.of(0, 5)));
        model.addAttribute("frontendPosts", postService.getPostsByCategory(PostCategory.FRONTEND, PageRequest.of(0, 5)));
        model.addAttribute("jobPosts", postService.getPostsByCategory(PostCategory.JOB, PageRequest.of(0, 5)));

        return "main";
    }
}
