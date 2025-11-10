package com.codementor.mentoring.dto.mapper;

import com.codementor.member.entity.Member;
import com.codementor.mentoring.dto.MentoringBookingDto;
import com.codementor.mentoring.entity.MentoringBooking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringBookingMapper {

    MentoringBookingDto toDto(Member member);
    MentoringBooking toEntity(MentoringBookingDto dto);

}
