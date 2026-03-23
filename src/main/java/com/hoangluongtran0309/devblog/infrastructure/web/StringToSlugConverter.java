package com.hoangluongtran0309.devblog.infrastructure.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.content.shared.Slug;

@Component
public class StringToSlugConverter implements Converter<String, Slug> {

    @Override
    public Slug convert(String source) {
        return new Slug(source);
    }
}