package com.codementor.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditNicknameDto {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "[가-힣a-zA-Z0-9]{2,10}", message = "특수문자를 제외한 2 ~ 10자의 문자여야합니다.")
    private String nickname;
}
