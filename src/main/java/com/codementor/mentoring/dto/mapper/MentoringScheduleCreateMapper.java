package com.codementor.mentoring.dto.mapper;

import com.codementor.mentoring.dto.MentoringScheduleCreate;
import com.codementor.mentoring.entity.MentoringSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringScheduleCreateMapper {

    MentoringSchedule toEntity(MentoringScheduleCreate mentoringScheduleCreate);
}
