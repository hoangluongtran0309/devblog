package com.hoangluongtran0309.devblog.media.web;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.media.MediaId;

@Component
public class StringToMediaIdConverter implements Converter<String, MediaId> {

    @Override
    public MediaId convert(String source) {
        return new MediaId(UUID.fromString(source));
    }
}
