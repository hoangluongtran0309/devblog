package com.hoangluongtran0309.devblog.media;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface MediaServicePort {

    Map<String, Object> upload(MultipartFile file, String folder);

    void delete(String publicId);
}
