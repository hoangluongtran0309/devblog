package com.hoangluongtran0309.devblog.media;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "db_media")
public class Media extends BaseEntity<MediaId> {

    @Embedded
    private MediaPublicId mediaPublicId;

    @Embedded
    private MediaUrl mediaUrl;

    @Embedded
    private MediaFileName mediaFileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType;

    private long fileSize;

    protected Media() {

    }

    public Media(MediaId id, MediaPublicId mediaPublicId, MediaUrl mediaUrl, MediaFileName mediaFileName,
            MediaType mediaType, long fileSize) {
        super(id);
        this.mediaPublicId = mediaPublicId;
        this.mediaUrl = mediaUrl;
        this.mediaFileName = mediaFileName;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
    }

    public MediaPublicId getMediaPublicId() {
        return mediaPublicId;
    }

    public void setMediaPublicId(MediaPublicId mediaPublicId) {
        this.mediaPublicId = mediaPublicId;
    }

    public MediaUrl getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(MediaUrl mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public MediaFileName getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(MediaFileName mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
