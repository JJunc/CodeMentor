package com.codementor.member.enums;

public enum CheckEmail {
    SUCCESS("SUCCESS", "이메일이 성공적으로 변경되었습니다."),
    DUPLICATED_EMAIL("DUPLICATED_EMAIL", "이미 사용중인 이메일 입니다.");

    private String errorCode;
    private String message;

    CheckEmail(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }
    public String getErrorCode() {
        return errorCode;
    }
}
