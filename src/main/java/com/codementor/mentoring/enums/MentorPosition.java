package com.codementor.mentoring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MentorPosition {

    FRONTEND("프론트"),
    BACKEND("백엔드");

    private final String position;
}
