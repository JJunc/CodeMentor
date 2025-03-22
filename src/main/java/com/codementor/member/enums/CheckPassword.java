package com.codementor.member.enums;

public enum CheckPassword    {
    SUCCESS("SUCCESS", "비밀번호가 성공적으로 변경되었습니다."),
    CURRENT_PASSWORD_MISMATCH("CURRENT_PASSWORD_MISMATCH", "현재 비밀번호가 일치하지 않습니다."),
    PASSWORDS_DO_NOT_MATCH("PASSWORDS_DO_NOT_MATCH", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVALID_NEW_PASSWORD("INVALID_NEW_PASSWORD", "새 비밀번호는 규격에 맞지 않습니다."),
    SAME_AS_OLD("PASSWORD_SAME_AS_OLD", "현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");

    private String errorCode;
    private String message;

    CheckPassword(String errorCode, String message) {
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
