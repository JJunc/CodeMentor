package com.codementor.mentor.entity;

import com.codementor.mentor.dto.MentoringBookingDto;
import com.codementor.mentor.enums.MentoringBookingStatus;
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

    @Builder
    public MentoringBooking(Long mentorId, Long menteeId, Long mentoringScheduleId,
                            BigDecimal price, LocalDateTime startTime, LocalDateTime endTime, MentoringBookingStatus status) {
        this.mentorId = mentorId;
        this.menteeId = menteeId;
        this.mentoringScheduleId = mentoringScheduleId;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

}
