package com.codementor.mentor.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MentoringHashtagConverter implements AttributeConverter<MentoringHashtagName, String> {

    @Override
    public String convertToDatabaseColumn(MentoringHashtagName mentoringHashtagName) {
        return mentoringHashtagName.tagName;
    }

    @Override
    public MentoringHashtagName convertToEntityAttribute(String tagName) {
        return MentoringHashtagName.fromTagName(tagName);
    }
}
