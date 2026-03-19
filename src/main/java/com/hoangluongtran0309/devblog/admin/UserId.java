package com.hoangluongtran0309.devblog.admin;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserId extends BaseId<UUID> {

    protected UserId() {

    }

    private UserId(UUID id) {
        super(id);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}
