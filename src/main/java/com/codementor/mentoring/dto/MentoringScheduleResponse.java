package com.codementor.mentoring.dto;

import com.codementor.mentoring.enums.MentoringBookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MentoringScheduleResponse {
    private String startTime;
    private String endTime;
    private boolean available;
}
