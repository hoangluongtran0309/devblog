package com.hoangluongtran0309.devblog.content.project.web;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.tag.TagId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AbstractProjectFormData {

    @NotBlank(message = "Project name must not be blank")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    private String projectName;

    @NotBlank(message = "Project summary must not be blank")
    @Size(max = 500, message = "Project summary must not exceed 500 characters")
    private String projectSummary;

    @NotBlank(message = "Project content must not be blank")
    private String projectContent;

    @Size(max = 500, message = "Image preview URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?:\\/\\/.*)?$", message = "Image preview must be a valid URL")
    private String imagePreview;

    @NotBlank(message = "Project slug must not be blank")
    @Size(min = 3, max = 255, message = "Slug must be between 3 and 255 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String projectSlug;

    @NotEmpty(message = "At least one tag is required")
    private Set<@NotNull(message = "Tag must not be null") TagId> tagIds;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSummary() {
        return projectSummary;
    }

    public void setProjectSummary(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }

    public String getProjectSlug() {
        return projectSlug;
    }

    public void setProjectSlug(String projectSlug) {
        this.projectSlug = projectSlug;
    }

    public Set<TagId> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<TagId> tagIds) {
        this.tagIds = tagIds;
    }
}