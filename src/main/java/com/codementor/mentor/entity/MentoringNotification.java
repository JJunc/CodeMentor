package com.codementor.mentor.entity;

import com.codementor.mentor.enums.MentoringNotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MentoringNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MentoringNotificationType type;

    private Long receiverId;

    @Column(nullable = false)
    private Long bookingId;

    private String message;

    private boolean isRead = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();
}
