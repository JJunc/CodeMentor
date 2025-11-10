package com.codementor.mentoring.dto.mapper;

import com.codementor.mentoring.dto.MentoringPostListDto;
import com.codementor.mentoring.entity.MentoringPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MentoringPostListMapper {

    List<MentoringPostListDto> toDtoList(List<MentoringPost> mentoringPostList);

}
