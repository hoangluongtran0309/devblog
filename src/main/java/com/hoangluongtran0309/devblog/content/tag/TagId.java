package com.hoangluongtran0309.devblog.content.tag;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class TagId extends BaseId<UUID> {

    protected TagId() {

    }

    public TagId(UUID id) {
        super(id);
    }

    public static TagId generate() {
        return new TagId(UUID.randomUUID());
    }
}
