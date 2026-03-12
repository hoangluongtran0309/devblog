package com.hoangluongtran0309.devblog.infrastructure.persistence;

import java.util.Objects;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseId<T> {

    @Column
    protected T id;

    protected BaseId() {

    }

    protected BaseId(T id) {
        this.id = Objects.requireNonNull(id);
    }

    public T asString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BaseId<?> baseId = (BaseId<?>) o;

        return id.equals(baseId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}