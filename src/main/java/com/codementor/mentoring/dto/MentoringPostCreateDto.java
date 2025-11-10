package com.codementor.mentoring.dto;

import com.codementor.mentoring.enums.MentorCareer;
import com.codementor.mentoring.enums.MentorPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringPostCreateDto {

    private Long authorId;

    private String mentorUsername;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;

    private Integer durationHorus;

    @Positive
    private Integer durationMinutes;

    @Positive
    private BigDecimal price;

    private MentorPosition mentorPosition;

    private MentorCareer mentorCareer;

}

