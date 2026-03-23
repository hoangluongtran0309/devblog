package com.hoangluongtran0309.devblog.content.project;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagId;

public class UpdateProjectParameters extends CreateProjectParameters {

    private final long version;

    public UpdateProjectParameters(long version, ProjectName projectName, ProjectSummary projectSummary,
            ProjectContent projectContent, ImagePreview imagePreview, Slug projectSlug, Set<TagId> tagIds) {
        super(projectName, projectSummary, projectContent, imagePreview, projectSlug, tagIds);
        this.version = version;
    }

    public long getVersion() {
        return version;
    }
}
