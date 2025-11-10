package com.codementor.mentoring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MentorCareer {

    JUNIOR("주니어(1~3년)"),
    MIDDLE("미들(4~8년)"),
    SENIOR("시니어(9년 이상)"),
    LEAD("Lead 레벨");

    private final String label;
}
