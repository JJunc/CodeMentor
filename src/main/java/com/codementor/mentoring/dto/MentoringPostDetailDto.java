package com.codementor.mentoring.dto;

import com.codementor.mentoring.enums.MentorCareer;
import com.codementor.mentoring.enums.MentorPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class MentoringPostDetailDto {

    private Long id;
    private String title;
    private String mentorNickname;
    private String content;
    private MentorCareer mentorCareer;
    private MentorPosition mentorPosition;
    private BigDecimal price;
    private Integer durationHours;
    private Integer durationMinutes;

}
