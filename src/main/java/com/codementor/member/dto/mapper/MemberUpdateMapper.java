package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MemberEditPasswordDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberUpdateMapper {

    MemberEditPasswordDto toDto(Member member);
}
