package com.codementor.mentor.enums;

public enum MentoringBookingStatus {

    WAITING("대기"),        // 예약 신청 접수
    CONFIRMED("확정"),
    COMPLETED("완료"),      // 멘토링 종료
    CANCELED("취소");       // 신청자나 멘토가 예약을 취소


    private final String description;

    MentoringBookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
