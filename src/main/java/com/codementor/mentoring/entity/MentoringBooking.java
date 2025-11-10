package com.codementor.mentoring.entity;

import com.codementor.mentoring.enums.MentoringBookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentoringBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mentorId;

    @Column(nullable = false)
    private Long menteeId;

    @Column(nullable = false, unique = true)
    private Long mentoringScheduleId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentoringBookingStatus status;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime updatedAt;

}
