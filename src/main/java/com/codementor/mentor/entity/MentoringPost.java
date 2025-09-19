package com.codementor.mentor.entity;

import com.codementor.mentor.dto.MentoringPostCreateDto;
import com.codementor.mentor.enums.MentorPosition;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Entity
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void createMentoringPost(MentoringPostCreateDto dto){
        this.id = dto.getAuthorId();
        this.mentorNickname = dto.getAuthorNickname();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.durationMinutes = dto.getDurationMinutes();
        this.price = dto.getPrice();
    }
}
