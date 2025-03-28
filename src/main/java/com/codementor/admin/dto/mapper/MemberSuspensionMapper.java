package com.codementor.admin.dto.mapper;

import com.codementor.admin.dto.MemberSuspensionDto;
import com.codementor.admin.entity.MemberSuspension;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberSuspensionMapper {

    MemberSuspensionDto toDto(MemberSuspension memberSuspension);

    @Mapping(source = "memberId", target = "member.id")
    MemberSuspension toEntity(MemberSuspensionDto memberSuspensionDto);
}
