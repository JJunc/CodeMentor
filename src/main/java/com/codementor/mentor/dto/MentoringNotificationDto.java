package com.codementor.mentor.dto;

import com.codementor.mentor.enums.MentoringNotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentoringNotificationDto {

    private Long receiverId;

    private Long bookingId;

    private MentoringNotificationType type;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();

}
