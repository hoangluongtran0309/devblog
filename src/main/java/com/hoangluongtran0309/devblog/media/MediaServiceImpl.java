package com.hoangluongtran0309.devblog.media;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);

    private static final long MAX_FILE_SIZE = 50L * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif",
            "video/mp4",
            "video/quicktime");

    private final MediaRepository mediaRepository;
    private final MediaServicePort mediaServicePort;

    public MediaServiceImpl(MediaRepository mediaRepository, MediaServicePort mediaServicePort) {
        this.mediaRepository = mediaRepository;
        this.mediaServicePort = mediaServicePort;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Media> getById(MediaId mediaId) {
        return mediaRepository.findById(mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Media> getImages(Pageable pageable) {
        return mediaRepository.findByMediaType(MediaType.IMAGE, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Media> getVideos(Pageable pageable) {
        return mediaRepository.findByMediaType(MediaType.VIDEO, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Media> getAll(Pageable pageable) {
        return mediaRepository.findAll(pageable);
    }

    @Override
    public Media upload(MultipartFile file) {
        validateFile(file);

        logger.info("Uploading media file: {}", file.getOriginalFilename());

        var result = mediaServicePort.upload(file, "devblog/media");

        validateUploadResult(result);

        String resourceType = Optional.ofNullable((String) result.get("resourceType")).orElse("image");
        var mediaType = "video".equals(resourceType) ? MediaType.VIDEO : MediaType.IMAGE;

        String urlStr = (String) result.get("url");
        validateUrl(urlStr);

        var media = new Media(
                MediaId.generate(),
                new MediaPublicId((String) result.get("publicId")),
                new MediaUrl(urlStr),
                new MediaFileName((String) result.get("originalFilename")),
                mediaType,
                ((Number) result.get("bytes")).longValue());

        Media saved = mediaRepository.save(media);
        logger.info("Media file uploaded successfully, ID: {}", saved.getId().asString());
        return saved;
    }

    @Override
    public void delete(MediaId mediaId) {
        logger.info("Deleting media file, ID: {}", mediaId.asString());

        Media media = getById(mediaId)
                .orElseThrow(() -> new MediaNotFoundException(mediaId));

        mediaServicePort.delete(media.getMediaPublicId().asString());
        mediaRepository.deleteById(media.getId());

        logger.info("Media file deleted successfully, ID: {}", mediaId.asString());
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException(file.getSize());
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new UnsupportedMediaTypeException(contentType, ALLOWED_CONTENT_TYPES);
        }
    }

    private void validateUploadResult(Map<String, Object> result) {
        if (result == null) {
            throw new BusinessException("Upload failed: empty response from storage provider");
        }

        for (String key : new String[] { "publicId", "url", "originalFilename", "bytes" }) {
            if (result.get(key) == null) {
                throw new BusinessException("Upload failed: missing field '%s' in storage response".formatted(key));
            }
        }
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);

            if (!"https".equals(uri.getScheme())) {
                throw new BusinessException("Media URL must use HTTPS");
            }

        } catch (URISyntaxException e) {
            throw new BusinessException("Invalid media URL returned from storage provider: " + url);
        }
    }
}