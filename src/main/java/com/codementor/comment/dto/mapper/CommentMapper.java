package com.codementor.comment.dto.mapper;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author", target = "member.username")
    @Mapping(source = "postId", target = "post.id")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "member.username", target = "author")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "post.title", target = "postDetail.title")
    @Mapping(source = "post.category", target = "postDetail.category")
    @Mapping(source = "post.author.username", target = "postDetail.author")
    @Mapping(source = "parent.id", target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "content", expression = "java(comment.getIsDeleted().equals(\"Y\") ? \"삭제된 댓글입니다.\" : comment.getContent())")
    CommentDto toDto(Comment comment);

}
