package com.codementor.member.dto;

import com.codementor.member.enums.MemberRole;
import com.codementor.member.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchDto {

    private Long id;
    private String keyword;
    private String username;
    private String nickname;
    private String email;
    private MemberStatus searchStatus;
    private MemberRole role;

}
