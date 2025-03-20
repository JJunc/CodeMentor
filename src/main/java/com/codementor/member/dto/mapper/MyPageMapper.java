package com.codementor.member.dto.mapper;

import com.codementor.member.dto.MyPageDto;
import com.codementor.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MyPageMapper {

    MyPageDto toDto(Member member);
}
