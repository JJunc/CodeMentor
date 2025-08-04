package com.codementor.post.dto.mapper;

import com.codementor.post.dto.PostDeleteDto;
import com.codementor.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostDeleteMapper {

    PostDeleteDto toDto(Post post);

    Post toEntity(PostDeleteDto postDeleteDto);
}
