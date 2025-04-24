package com.codementor.comment.dto.mapper;

import com.codementor.comment.dto.CommentRequestDto;
import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment requestToEntity(CommentRequestDto commentRequestDto);

    @Mapping(source = "parent.id", target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "content", expression = "java(comment.getIsDeleted().equals(\"Y\") ? \"삭제된 댓글입니다.\" : comment.getContent())")
    CommentResponseDto toResponseDto(Comment comment);

}
