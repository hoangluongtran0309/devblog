package com.hoangluongtran0309.devblog.content.category.web;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.content.category.CategoryId;

@Component
public class StringToCategoryIdConverter implements Converter<String, CategoryId> {

    @Override
    public CategoryId convert(String source) {
        return new CategoryId(UUID.fromString(source));
    }
}
