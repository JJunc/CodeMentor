package com.codementor.mentoring.dto.mapper;

import com.codementor.mentoring.dto.MentoringPostDetailDto;
import com.codementor.mentoring.entity.MentoringPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringPostDetailMapper {

    MentoringPostDetailDto toDto(MentoringPost post);
}
