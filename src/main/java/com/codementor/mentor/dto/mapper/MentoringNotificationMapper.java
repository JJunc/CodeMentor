package com.codementor.mentor.dto.mapper;

import com.codementor.mentor.dto.MentoringNotificationDto;
import com.codementor.mentor.entity.MentoringNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoringNotificationMapper {

    MentoringNotification toEntity(MentoringNotificationDto dto);
    MentoringNotificationDto toDto(MentoringNotificationDto dto);
}
