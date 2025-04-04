package com.codementor.admin.dto;

import com.codementor.member.enums.MemberRole;
import com.codementor.member.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberUpdateDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private MemberStatus status;
    private MemberRole role;
    private LocalDateTime createdAt;
}
