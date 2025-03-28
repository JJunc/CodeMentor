package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MemberListDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberListMapper {

    MemberListDto toDto(Member member);
}
