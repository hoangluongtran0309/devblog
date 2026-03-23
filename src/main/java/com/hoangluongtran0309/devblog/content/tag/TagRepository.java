package com.hoangluongtran0309.devblog.content.tag;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.Slug;

@Repository
@Transactional(readOnly = true)
public interface TagRepository
        extends CrudRepository<Tag, TagId>, TagRepositoryCustom, PagingAndSortingRepository<Tag, TagId> {

    boolean existsByTagSlug(Slug tagSlug);

    boolean existsByTagName(TagName tagName);

    Optional<Tag> findByTagSlug(Slug tagSlug);
}
