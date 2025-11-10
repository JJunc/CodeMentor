package com.codementor.mentoring.entity;

import com.codementor.mentoring.dto.MentoringPostCreateDto;
import com.codementor.mentoring.enums.MentorCareer;
import com.codementor.mentoring.enums.MentorPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MentoringPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int mentorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorPosition mentorPosition;

    private String mentorNickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorCareer mentorCareer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Integer durationHours;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
