package com.codementor.member.dto;

import com.codementor.member.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoleDto {

    private Long id;
    private String nickname;
    private MemberRole memberRole;
}
