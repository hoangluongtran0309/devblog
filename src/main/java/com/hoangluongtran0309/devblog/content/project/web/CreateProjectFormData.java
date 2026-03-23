package com.hoangluongtran0309.devblog.content.project.web;

import com.hoangluongtran0309.devblog.content.project.CreateProjectParameters;
import com.hoangluongtran0309.devblog.content.project.ProjectContent;
import com.hoangluongtran0309.devblog.content.project.ProjectName;
import com.hoangluongtran0309.devblog.content.project.ProjectSummary;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;

public class CreateProjectFormData extends AbstractProjectFormData {

    public CreateProjectParameters toParameters() {
        return new CreateProjectParameters(
                new ProjectName(getProjectName()),
                new ProjectSummary(getProjectSummary()),
                new ProjectContent(getProjectContent()),
                new ImagePreview(getImagePreview()),
                new Slug(getProjectSlug()),
                getTagIds());
    }
}
