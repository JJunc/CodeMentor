package com.codementor.member.dto.mapper;

import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    LoginResponseDto toDto (Member member);
    Member toEntity (LoginResponseDto dto);
}
