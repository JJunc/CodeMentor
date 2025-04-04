package com.codementor.exception;

public class ErrorDto {
    private String code;      // 오류 코드 (예: "USER_NOT_FOUND", "INVALID_PASSWORD")
    private String message;   // 오류 메시지 (예: "아이디 또는 비밀번호가 맞지 않습니다.")
    private String details;   // 오류에 대한 추가적인 세부 정보 (선택 사항)

    // 생성자, Getter, Setter 생략

    public ErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorDto(String code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
