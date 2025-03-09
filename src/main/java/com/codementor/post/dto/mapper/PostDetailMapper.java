package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.dto.PostDetailDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostDetailMapper {

    PostDetailMapper INSTANCE = Mappers.getMapper(PostDetailMapper.class);

    @Mapping(source = "author.username", target = "authorName")
    PostDetailDto toDto(Post post);

    @Mapping(source = "authorName", target = "author.username")
    Post toEntity(PostDetailDto postDetailDto);
}
