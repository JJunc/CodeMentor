package com.codementor.reply.dto.mapper;

import com.codementor.comment.entity.Comment;
import com.codementor.reply.dto.ReplyDto;
import com.codementor.reply.entity.Reply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReplyMapper {

    @Mapping(source = "author", target = "member.username")
    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "parentId", target = "parent.id")
    Reply toEntity(ReplyDto dto);

    @Mapping(source = "member.username", target = "author")
    @Mapping(source="post.id", target = "postId")
    @Mapping(source="parent.id", target="parentId")
    ReplyDto toDto(Reply reply);
}
