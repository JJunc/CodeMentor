package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MemberUpdateDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberUpdateMapper {

    MemberUpdateDto toDto(Member member);
}
