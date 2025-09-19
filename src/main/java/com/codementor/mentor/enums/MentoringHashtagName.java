package com.codementor.mentor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MentoringHashtagName {

    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    DATA("데이터사이언스"),
    DEVOPS("데브옵스"),
    AI("AI"),
    JAVA("Java"),
    NODE("Node.js"),
    JAVASCRIPT("JavaScript"),
    RESUME("이력서"),
    PORTFOLIO("포트폴리오"),
    JOB("취업");

    String tagName;

    public static MentoringHashtagName fromTagName(String tagName) {
        for (MentoringHashtagName name : values()) {
            if (name.tagName.equals(tagName)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Unknown tagName: " + tagName);
    }
}
