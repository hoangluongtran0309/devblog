package com.hoangluongtran0309.devblog.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
public class FileSizeExceededException extends BusinessException {

    public FileSizeExceededException(long fileSize) {
        super("File size %.2f MB exceeds the maximum allowed size of 50 MB"
                .formatted(fileSize / (1024.0 * 1024.0)));
    }
}