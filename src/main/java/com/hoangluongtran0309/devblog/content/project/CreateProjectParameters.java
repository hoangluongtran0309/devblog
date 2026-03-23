package com.hoangluongtran0309.devblog.content.project;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagId;

public class CreateProjectParameters {

    private final ProjectName projectName;
    private final ProjectSummary projectSummary;
    private final ProjectContent projectContent;
    private final ImagePreview imagePreview;
    private final Slug projectSlug;
    private final Set<TagId> tagIds;

    public CreateProjectParameters(ProjectName projectName, ProjectSummary projectSummary,
            ProjectContent projectContent, ImagePreview imagePreview, Slug projectSlug,
            Set<TagId> tagIds) {
        this.projectName = projectName;
        this.projectSummary = projectSummary;
        this.projectContent = projectContent;
        this.imagePreview = imagePreview;
        this.projectSlug = projectSlug;
        this.tagIds = tagIds;
    }

    public ProjectName getProjectName() {
        return projectName;
    }

    public ProjectSummary getProjectSummary() {
        return projectSummary;
    }

    public ProjectContent getProjectContent() {
        return projectContent;
    }

    public ImagePreview getImagePreview() {
        return imagePreview;
    }

    public Slug getProjectSlug() {
        return projectSlug;
    }

    public Set<TagId> getTagIds() {
        return tagIds;
    }
}
