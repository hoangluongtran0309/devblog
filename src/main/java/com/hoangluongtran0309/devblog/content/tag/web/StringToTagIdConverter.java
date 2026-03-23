package com.hoangluongtran0309.devblog.content.tag.web;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.content.tag.TagId;

@Component
public class StringToTagIdConverter implements Converter<String, TagId> {

    @Override
    public TagId convert(String source) {
        return new TagId(UUID.fromString(source));
    }
}
