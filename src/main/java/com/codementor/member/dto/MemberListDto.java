package com.codementor.member.dto;

import com.codementor.member.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private MemberStatus status;
    private String isDeleted;
    private LocalDateTime createdAt;

}
