package com.hoangluongtran0309.devblog.content.project.web;

import java.util.stream.Collectors;

import com.hoangluongtran0309.devblog.content.project.Project;
import com.hoangluongtran0309.devblog.content.project.ProjectContent;
import com.hoangluongtran0309.devblog.content.project.ProjectName;
import com.hoangluongtran0309.devblog.content.project.ProjectSummary;
import com.hoangluongtran0309.devblog.content.project.UpdateProjectParameters;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;

public class UpdateProjectFormData extends AbstractProjectFormData {

    private String id;
    private long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public UpdateProjectParameters toParameters() {
        return new UpdateProjectParameters(
                version,
                new ProjectName(getProjectName()),
                new ProjectSummary(getProjectSummary()),
                new ProjectContent(getProjectContent()),
                new ImagePreview(getImagePreview()),
                new Slug(getProjectSlug()),
                getTagIds());
    }

    public static UpdateProjectFormData fromData(Project project) {
        UpdateProjectFormData result = new UpdateProjectFormData();

        result.setId(project.getId().asString().toString());
        result.setProjectName(project.getProjectName().asString());
        result.setProjectSummary(project.getProjectSummary().asString());
        result.setProjectContent(project.getProjectContent().asString());
        result.setImagePreview(project.getImagePreview().asString());
        result.setProjectSlug(project.getProjectSlug().asString());
        result.setTagIds(project.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        result.setVersion(project.getVersion());

        return result;
    }
}
