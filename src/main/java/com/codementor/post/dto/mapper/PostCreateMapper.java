package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostCreateMapper {

    PostCreateMapper INSTANCE = Mappers.getMapper(PostCreateMapper.class);

    @Mapping(source = "author.username", target = "author")
    PostCreateDto toDto(Post post);

    @Mapping(source = "author", target = "author.username")
    Post toEntity(PostCreateDto postCreateDto);

}
