package com.codementor.member.dto;

import com.codementor.admin.dto.MemberSuspensionDto;
import com.codementor.member.enums.MemberRole;
import com.codementor.member.enums.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    private String username;
    private MemberStatus status;
    private String reason;
    private MemberRole role;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
