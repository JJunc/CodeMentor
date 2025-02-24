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
            return PostCategory.valueOf(source.toUpperCase()); // 대소문자 구분 없이 변환
        } catch (IllegalArgumentException e) {
            return PostCategory.FREE; // 기본값 설정
        }
    }

}
