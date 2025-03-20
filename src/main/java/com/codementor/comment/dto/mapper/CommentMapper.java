package com.codementor.comment.dto.mapper;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author", target = "member.username")
    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "replies", target = "replies")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "member.username", target = "author")
    @Mapping(source="post.id", target = "postId")
    @Mapping(source = "replies", target = "replies")
    @Mapping(expression = "java(comment.getReplies() != null ? comment.getReplies().size() : 0)", target = "replyCount")
    CommentDto toDto(Comment comment);

}
