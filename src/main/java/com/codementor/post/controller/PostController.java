package com.codementor.post.controller;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @GetMapping
    public String posts(Model model) {
        model.addAttribute("posts", new ArrayList<PostListDto>());
        return "/post/posts";
    }
}
