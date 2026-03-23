package com.hoangluongtran0309.devblog.content.project;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "db_project")
public class Project extends BaseEntity<ProjectId> {

    @Embedded
    @NotNull
    private ProjectName projectName;

    @Embedded
    @NotNull
    private ProjectSummary projectSummary;

    @Embedded
    @NotNull
    private ProjectContent projectContent;

    @Embedded
    @NotNull
    private ImagePreview imagePreview;

    @Embedded
    @AttributeOverride(name = "slug", column = @Column(name = "project_slug"))
    @NotNull
    private Slug projectSlug;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ContentStatus contentStatus;

    private LocalDateTime publishedAt;

    @ManyToMany
    @JoinTable(name = "project_tag", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Version
    private long version;

    protected Project() {

    }

    public Project(ProjectId id, ProjectName projectName, ProjectSummary projectSummary, ProjectContent projectContent,
            ImagePreview imagePreview, Slug projectSlug, ContentStatus contentStatus, Set<Tag> tags) {
        super(id);
        this.projectName = projectName;
        this.projectSummary = projectSummary;
        this.projectContent = projectContent;
        this.imagePreview = imagePreview;
        this.projectSlug = projectSlug;
        this.contentStatus = contentStatus;
        this.tags = tags;
    }

    public ProjectName getProjectName() {
        return projectName;
    }

    public void setProjectName(ProjectName projectName) {
        this.projectName = projectName;
    }

    public ProjectSummary getProjectSummary() {
        return projectSummary;
    }

    public void setProjectSummary(ProjectSummary projectSummary) {
        this.projectSummary = projectSummary;
    }

    public ProjectContent getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(ProjectContent projectContent) {
        this.projectContent = projectContent;
    }

    public ImagePreview getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(ImagePreview imagePreview) {
        this.imagePreview = imagePreview;
    }

    public Slug getProjectSlug() {
        return projectSlug;
    }

    public void setProjectSlug(Slug projectSlug) {
        this.projectSlug = projectSlug;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
