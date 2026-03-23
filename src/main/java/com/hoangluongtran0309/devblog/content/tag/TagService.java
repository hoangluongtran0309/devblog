package com.hoangluongtran0309.devblog.content.tag;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public interface TagService {

    boolean existsBySlug(Slug tagSlug);

    boolean existsByName(TagName tagName);

    Optional<Tag> getBySlug(Slug tagSlug);

    Optional<Tag> getById(TagId tagId);

    Page<Tag> getAll(Pageable pageable);

    Iterable<Tag> getAll();

    Iterable<Tag> getAllById(Iterable<TagId> tagIds);

    Tag create(TagParameters parameters);

    Tag update(TagId tagId, TagParameters parameters);

    void delete(TagId tagId);
}