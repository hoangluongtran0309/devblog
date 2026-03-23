package com.hoangluongtran0309.devblog.content.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public interface ProjectService {

    boolean existsBySlug(Slug projectSlug);

    boolean existsByName(ProjectName projectName);

    Optional<Project> getBySlug(Slug projectSlug);

    Optional<Project> getById(ProjectId projectId);

    Page<Project> getPublishedProjects(Pageable pageable);

    Page<Project> getPublishedProjectsByTag(Slug tagSlug, Pageable pageable);

    Page<Project> searchPublishedProjects(String keyword, Pageable pageable);

    Page<Project> getAll(Pageable pageable);

    Project create(CreateProjectParameters parameters);

    Project update(ProjectId projectId, UpdateProjectParameters parameters);

    Project publish(ProjectId projectId);

    void delete(ProjectId projectId);
}
