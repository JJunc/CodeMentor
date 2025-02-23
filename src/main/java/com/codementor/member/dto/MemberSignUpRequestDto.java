package com.codementor.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    String password;

    @NotBlank(message = "비밀번호를 확인해주세요")
    String confirmPassword;

    @NotBlank(message="이메일을 입력해주세요")
    String email;

}
