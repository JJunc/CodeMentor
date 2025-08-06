package com.codementor.post.enums;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostCategoryConverter implements Converter<String, PostCategory> {

    @Override
    public PostCategory convert(String source) {
        try {
            return PostCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PostCategory.FREE;
        }
    }

}
