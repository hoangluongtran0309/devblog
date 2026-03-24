package com.hoangluongtran0309.devblog.media;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class MediaId extends BaseId<UUID> {

    protected MediaId() {

    }

    public MediaId(UUID id) {
        super(id);
    }

    public static MediaId generate() {
        return new MediaId(UUID.randomUUID());
    }
}
