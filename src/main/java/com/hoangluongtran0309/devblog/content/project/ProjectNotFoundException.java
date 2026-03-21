package com.hoangluongtran0309.devblog.content.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends NotFoundException {

    public ProjectNotFoundException(ProjectId projectId) {
        super("Project %s not found".formatted(projectId.asString()));
    }
}