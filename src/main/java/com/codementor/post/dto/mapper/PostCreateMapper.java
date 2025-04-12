package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostCreateMapper {

    PostCreateMapper INSTANCE = Mappers.getMapper(PostCreateMapper.class);

    PostCreateDto toDto(Post post);

    Post toEntity(PostCreateDto postCreateDto);

}
