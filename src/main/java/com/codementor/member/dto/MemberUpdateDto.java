package com.codementor.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    private String username;
    private String nickname;

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")  // 길이 검사는 별도로 수행
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).*$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")  // 길이 검사는 별도로 수행
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).*$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "새 비밀번호를 확인해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")  // 길이 검사는 별도로 수행
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).*$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String confirmPassword;

    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    private String url;
    private LocalDateTime createdAt;

}
