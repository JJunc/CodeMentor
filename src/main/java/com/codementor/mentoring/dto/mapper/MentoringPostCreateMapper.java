package com.codementor.mentoring.dto.mapper;

import com.codementor.mentoring.dto.MentoringPostCreateDto;
import com.codementor.mentoring.entity.MentoringPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringPostCreateMapper {

    MentoringPost toEntity(MentoringPostCreateDto dto);
}
