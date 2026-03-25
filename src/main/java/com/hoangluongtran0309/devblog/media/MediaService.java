package com.hoangluongtran0309.devblog.media;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    Optional<Media> getById(MediaId mediaId);

    Page<Media> getImages(Pageable pageable);

    Page<Media> getVideos(Pageable pageable);

    Page<Media> getAll(Pageable pageable);

    Media upload(MultipartFile file);

    void delete(MediaId mediaId);
}
