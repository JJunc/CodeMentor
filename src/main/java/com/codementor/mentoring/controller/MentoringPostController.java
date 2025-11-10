package com.codementor.mentoring.controller;

import com.codementor.mentoring.dto.MentoringBookingDto;
import com.codementor.mentoring.dto.MentoringPostCreateDto;
import com.codementor.mentoring.service.MentoringBookingService;
import com.codementor.mentoring.service.MentoringPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mentoring")
@RequiredArgsConstructor
public class MentoringPostController {

    private final MentoringBookingService mentoringBookingService;
    private final MentoringPostService mentoringPostService;

    @GetMapping("/mentors")
    public String mentors(Model model) {

        model.addAttribute("dto", mentoringPostService.getMentoringPostList());

        return "/mentoring/mentoring-posts";
    }

    @GetMapping("/mentors/{postId}")
    public String mentoringDetails(@PathVariable Long postId, Model model) {
        model.addAttribute("dto", mentoringPostService.getMentoringPostDetail(postId));

        return "/mentoring/mentoring-post-detail";
    }

    @GetMapping("/search")
    public String search(Model model) {


        return "/mentoring/mentoring-posts";
    }

    @GetMapping("/post/create")
    public String createPost(Model model) {
        model.addAttribute("dto", new MentoringPostCreateDto());
        return "/mentoring/mentoring-post-create";
    }

    @PostMapping("/post/create")
    public String createPost(@ModelAttribute("dto") MentoringPostCreateDto dto) {
        mentoringPostService.createMentoringPost(dto);

        return "redirect:/mentoring/mentors";
    }

    @PostMapping("/booking")
    public String booking(@ModelAttribute MentoringBookingDto dto, Model model) {

        mentoringBookingService.bookMentoring(dto);

        return "redirect:/mentor";
    }
}
