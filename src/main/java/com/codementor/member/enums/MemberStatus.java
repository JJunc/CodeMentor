package com.codementor.member.enums;

public enum MemberStatus {
    ACTIVE("활동"),
    SUSPENDED("정지"),
    BANNED("영구정지");

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}