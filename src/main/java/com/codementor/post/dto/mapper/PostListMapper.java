package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostListMapper {

    PostListMapper INSTANCE = Mappers.getMapper(PostListMapper.class);

    @Mapping(source = "author.username", target = "author")
    PostListDto toDto(Post post);

    @Mapping(source = "author", target = "author.username")
    Post toEntity(PostListDto postListDto);
}
