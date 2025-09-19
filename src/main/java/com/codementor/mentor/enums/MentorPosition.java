package com.codementor.mentor.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MentorPosition {

    FRONTEND("프론트"),
    BACKEND("백엔드"),
    DATA("데이터");

    String position;
}
