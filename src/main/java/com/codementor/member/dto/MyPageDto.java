package com.codementor.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MyPageDto {

    private String username;
    private String nickname;
    private String email;
    private String currentPassword;
    private String password;
    private String confirmPassword;
    private LocalDateTime createdAt;
}
