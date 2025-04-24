package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostListMapper {

    PostListDto toDto(Post post);

    Post toEntity(PostListDto postListDto);
}
