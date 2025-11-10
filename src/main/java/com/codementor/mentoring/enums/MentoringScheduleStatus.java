package com.codementor.mentoring.enums;

public enum MentoringScheduleStatus {

    AVAILABLE("예약가능"),
    BOOKED("예약"),        // 예약 신청 접수
    COMPLETED("완료"),      // 멘토링 종료
    CANCELED("취소");       // 신청자나 멘토가 예약을 취소


    private final String description;

    MentoringScheduleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
