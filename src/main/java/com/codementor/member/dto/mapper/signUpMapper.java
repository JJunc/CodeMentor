package com.codementor.member.dto.mapper;

import com.codementor.member.dto.SignUpRequestDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface signUpMapper {

    SignUpRequestDto toDto(Member member);
    Member toEntity (SignUpRequestDto requestDto);

}
