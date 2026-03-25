package com.hoangluongtran0309.devblog.media;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface MediaRepository
                extends CrudRepository<Media, MediaId>, MediaRepositoryCustom,
                PagingAndSortingRepository<Media, MediaId> {

        Page<Media> findByMediaType(MediaType mediaType, Pageable pageable);
}
