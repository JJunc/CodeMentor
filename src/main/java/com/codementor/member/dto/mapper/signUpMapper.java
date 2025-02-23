package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MemberSignUpRequestDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface signUpMapper {

    MemberSignUpRequestDto toDto(Member member);
    Member toEntity (MemberSignUpRequestDto requestDto);

}
