package com.codementor.member.enums;

public enum MemberRole {

    MEMBER("일반회원"),
    MENTOR("멘토"),
    ADMIN("운영자");

    String role;

    MemberRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
