package com.codementor.mentor.dto.mapper;

import com.codementor.member.entity.Member;
import com.codementor.mentor.dto.MentoringBookingDto;
import com.codementor.mentor.entity.MentoringBooking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringBookingMapper {

    MentoringBookingDto toDto(Member member);
    MentoringBooking toEntity(MentoringBookingDto dto);

}
