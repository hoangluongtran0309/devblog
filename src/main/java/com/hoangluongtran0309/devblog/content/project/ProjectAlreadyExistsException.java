package com.hoangluongtran0309.devblog.content.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProjectAlreadyExistsException extends BusinessException {

    public ProjectAlreadyExistsException(Slug projectSlug) {
        super("Project with slug '%s' already exists".formatted(projectSlug.asString()));
    }

    public ProjectAlreadyExistsException(ProjectName projectName) {
        super("Project with title '%s' already exists".formatted(projectName.asString()));
    }
}
