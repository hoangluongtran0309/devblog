package com.hoangluongtran0309.devblog.content.project;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagService;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

        private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

        private final ProjectRepository projectRepository;
        private final TagService tagService;

        public ProjectServiceImpl(ProjectRepository projectRepository, TagService tagService) {
                this.projectRepository = projectRepository;
                this.tagService = tagService;
        }

        @Override
        @Transactional(readOnly = true)
        public boolean existsBySlug(Slug projectSlug) {
                return projectRepository.existsByProjectSlug(projectSlug);
        }

        @Override
        @Transactional(readOnly = true)
        public boolean existsByName(ProjectName projectName) {
                return projectRepository.existsByProjectName(projectName);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<Project> getBySlug(Slug projectSlug) {
                return projectRepository.findByProjectSlug(projectSlug);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<Project> getById(ProjectId projectId) {
                return projectRepository.findById(projectId);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Project> getPublishedProjects(Pageable pageable) {
                return projectRepository
                                .findByContentStatusOrderByPublishedAtDesc(
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Project> getPublishedProjectsByTag(Slug tagSlug, Pageable pageable) {
                return projectRepository
                                .findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
                                                tagSlug,
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Project> searchPublishedProjects(String keyword, Pageable pageable) {
                return projectRepository
                                .findByProjectName_ProjectNameContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
                                                keyword,
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Project> getAll(Pageable pageable) {
                return projectRepository.findAll(pageable);
        }

        @Override
        public Project create(CreateProjectParameters parameters) {
                logger.info("Creating draft project with name: {}", parameters.getProjectName());

                validateForCreate(parameters);

                Set<Tag> tags = getTags(parameters.getTagIds());

                Project project = new Project(
                                projectRepository.nextId(),
                                parameters.getProjectName(),
                                parameters.getProjectSummary(),
                                parameters.getProjectContent(),
                                parameters.getImagePreview(),
                                parameters.getProjectSlug(),
                                ContentStatus.DRAFT,
                                tags);

                logger.info("Draft project created with ID: {}", project.getId().asString());

                return projectRepository.save(project);
        }

        @Override
        public Project update(ProjectId projectId, UpdateProjectParameters parameters) {
                logger.info("Updating project with ID: {}", projectId.asString());

                Project project = getById(projectId)
                                .orElseThrow(() -> new ProjectNotFoundException(projectId));

                if (!isDraftProject(project)) {
                        throw new InvalidProjectStatusException(projectId, project.getContentStatus());
                }

                validateForUpdate(project, parameters);

                Set<Tag> tags = getTags(parameters.getTagIds());

                project.setProjectName(parameters.getProjectName());
                project.setProjectSummary(parameters.getProjectSummary());
                project.setProjectContent(parameters.getProjectContent());
                project.setImagePreview(parameters.getImagePreview());
                project.setProjectSlug(parameters.getProjectSlug());
                project.setTags(tags);

                logger.info("Project updated with ID: {}", projectId.asString());

                return projectRepository.save(project);
        }

        @Override
        public Project publish(ProjectId projectId) {
                logger.info("Publishing project with ID: {}", projectId.asString());

                Project project = getById(projectId)
                                .orElseThrow(() -> new ProjectNotFoundException(projectId));

                if (!isDraftProject(project)) {
                        throw new InvalidProjectStatusException(projectId, project.getContentStatus());
                }

                project.setContentStatus(ContentStatus.PUBLISHED);
                project.setPublishedAt(LocalDateTime.now());

                logger.info("Project published with ID: {}", projectId.asString());

                return projectRepository.save(project);
        }

        @Override
        public void delete(ProjectId projectId) {
                logger.info("Deleting project with ID: {}", projectId.asString());

                Project project = getById(projectId)
                                .orElseThrow(() -> new ProjectNotFoundException(projectId));

                projectRepository.delete(project);

                logger.info("Project deleted with ID: {}", projectId.asString());
        }

        private Set<Tag> getTags(Set<TagId> tagIds) {
                return StreamSupport.stream(tagService.getAllById(tagIds).spliterator(), false)
                                .collect(Collectors.toSet());
        }

        private boolean isDraftProject(Project project) {
                return project.getContentStatus() == ContentStatus.DRAFT;
        }

        private void validateForCreate(CreateProjectParameters parameters) {
                if (existsBySlug(parameters.getProjectSlug())) {
                        throw new ProjectAlreadyExistsException(parameters.getProjectSlug());
                }

                if (existsByName(parameters.getProjectName())) {
                        throw new ProjectAlreadyExistsException(parameters.getProjectName());
                }
        }

        private void validateForUpdate(Project project, UpdateProjectParameters parameters) {
                if (project.getVersion() != parameters.getVersion()) {
                        throw new ObjectOptimisticLockingFailureException(
                                        Project.class,
                                        project.getId().asString());
                }

                if (!project.getProjectSlug().equals(parameters.getProjectSlug())
                                && existsBySlug(parameters.getProjectSlug())) {
                        throw new ProjectAlreadyExistsException(parameters.getProjectSlug());
                }

                if (!project.getProjectName().equals(parameters.getProjectName())
                                && existsByName(parameters.getProjectName())) {
                        throw new ProjectAlreadyExistsException(parameters.getProjectName());
                }
        }
}