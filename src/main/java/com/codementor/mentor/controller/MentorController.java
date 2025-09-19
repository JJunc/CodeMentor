package com.codementor.mentor.controller;

import com.codementor.mentor.dto.MentoringBookingDto;
import com.codementor.mentor.service.MentoringBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorController {

    private final MentoringBookingService mentoringBookingService;

    @GetMapping("/mentors")
    public String mentors(Model model) {

        return "/mentoring/mentors";
    }

    @PostMapping("/booking")
    public String booking(@ModelAttribute MentoringBookingDto dto, Model model) {

        mentoringBookingService.bookMentoring(dto);

        return "redirect:/mentor";
    }
}
