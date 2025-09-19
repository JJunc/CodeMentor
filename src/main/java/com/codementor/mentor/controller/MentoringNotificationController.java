package com.codementor.mentor.controller;

import com.codementor.mentor.dto.MentoringNotificationDto;
import com.codementor.mentor.service.MentoringNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/MentoringNotifications")
public class MentoringNotificationController {

    private final MentoringNotificationService mentoringNotificationService;


    @PostMapping(value = "/send-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MentoringNotificationDto> createNotification(
            @RequestBody MentoringNotificationDto dto) {
        MentoringNotificationDto response = mentoringNotificationService.sendNotification(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/subscribe/{receiverId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long receiverId) {
        return mentoringNotificationService.subscribe(receiverId);
    }

}
