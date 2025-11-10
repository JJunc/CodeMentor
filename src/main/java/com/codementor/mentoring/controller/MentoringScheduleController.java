package com.codementor.mentoring.controller;

import com.codementor.mentoring.dto.MentoringScheduleCreate;
import com.codementor.mentoring.dto.MentoringScheduleResponse;
import com.codementor.mentoring.entity.MentoringSchedule;
import com.codementor.mentoring.service.MentoringScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/mentoring/schedule")
@RequiredArgsConstructor
public class MentoringScheduleController {

    private final MentoringScheduleService scheduleService;

    @GetMapping("/new")
    public String MentoringScheduleCreateForm(Model model) {
        model.addAttribute("dto", new MentoringScheduleCreate());
        return "/mentoring/mentoring-schedule";
    }

    @PostMapping("/new")
    public String newMentoringSchedule(@ModelAttribute MentoringScheduleCreate dto) {
        scheduleService.createMentoringSchedule(dto);
        return "redirect:/mentoring/mentoring-schedule";
    }

    @GetMapping
    public String MentoringScheduleList(Model model) {

    }

    @GetMapping("/{mentorId}/schedules")
    @ResponseBody
    public List<MentoringScheduleResponse> getSchedules(
            @PathVariable Long mentorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.getDailySchedules(mentorId, date);
    }
}
