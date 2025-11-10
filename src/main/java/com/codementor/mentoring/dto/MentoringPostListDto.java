package com.codementor.mentoring.dto;

import com.codementor.mentoring.enums.MentorCareer;
import com.codementor.mentoring.enums.MentorPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringPostListDto {
    private Long id;
    private String title;
    private String mentorNickname;
    private MentorCareer mentorCareer;
    private MentorPosition mentorPosition;
    private BigDecimal price;
    private Integer durationHours;
    private Integer durationMinutes;

}
