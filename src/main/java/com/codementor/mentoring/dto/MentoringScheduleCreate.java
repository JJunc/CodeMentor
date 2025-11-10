package com.codementor.mentoring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringScheduleCreate {
    private Long mentorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
}
