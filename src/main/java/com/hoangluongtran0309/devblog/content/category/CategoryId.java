package com.hoangluongtran0309.devblog.content.category;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class CategoryId extends BaseId<UUID> {

    protected CategoryId() {

    }

    public CategoryId(UUID id) {
        super(id);
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }
}
