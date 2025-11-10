package com.codementor.mentoring.dto;

import com.codementor.mentoring.enums.MentoringScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MentoringScheduleList {
    private String startTime;
    private String endTime;
    private MentoringScheduleStatus status;
}
