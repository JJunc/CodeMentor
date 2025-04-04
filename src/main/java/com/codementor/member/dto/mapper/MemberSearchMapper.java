package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MemberSearchDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSearchMapper {

    MemberSearchDto toDto(Member member);

    Member toEntity(MemberSearchDto dto);
}
