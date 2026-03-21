package com.hoangluongtran0309.devblog.content.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.Slug;

@Repository
@Transactional(readOnly = true)
public interface ProjectRepository extends
        CrudRepository<Project, ProjectId>, ProjectRepositoryCustom,
        PagingAndSortingRepository<Project, ProjectId> {

    boolean existsByProjectSlug(Slug projectSlug);

    boolean existsByProjectName(ProjectName projectName);

    Optional<Project> findByProjectSlug(Slug projectSlug);

    Page<Project> findByContentStatusOrderByPublishedAtDesc(ContentStatus contentStatus, Pageable pageable);

    Page<Project> findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
            Slug tagSlug,
            ContentStatus contentStatus,
            Pageable pageable);

    Page<Project> findByProjectName_ProjectNameContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
            String keyword,
            ContentStatus contentStatus,
            Pageable pageable);
}