package com.hoangluongtran0309.devblog.content.project;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProjectId extends BaseId<UUID> {

    protected ProjectId() {

    }

    private ProjectId(UUID id) {
        super(id);
    }

    public static ProjectId generate() {
        return new ProjectId(UUID.randomUUID());
    }
}