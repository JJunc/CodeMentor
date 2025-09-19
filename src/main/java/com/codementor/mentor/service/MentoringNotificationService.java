package com.codementor.mentor.service;

import com.codementor.mentor.dto.MentoringNotificationDto;
import com.codementor.mentor.dto.mapper.MentoringNotificationMapper;
import com.codementor.mentor.entity.MentoringNotification;
import com.codementor.mentor.repository.MentoringNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MentoringNotificationService {

    private final MentoringNotificationRepository mentoringNotificationRepository;
    private final MentoringNotificationMapper mentoringNotificationMapper;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public MentoringNotificationDto sendNotification(MentoringNotificationDto dto) {

        // DB 저장
        MentoringNotification notification = mentoringNotificationMapper.toEntity(dto);
        mentoringNotificationRepository.save(notification);

        // SSE 전송
        SseEmitter emitter = emitters.get(dto.getReceiverId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(dto, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitters.remove(dto.getReceiverId());
            }
        }

        return dto;
    }

    public SseEmitter subscribe(Long receiverId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(receiverId, emitter);

        emitter.onCompletion(() -> emitters.remove(receiverId));
        emitter.onTimeout(() -> emitters.remove(receiverId));

        return emitter;
    }
}
