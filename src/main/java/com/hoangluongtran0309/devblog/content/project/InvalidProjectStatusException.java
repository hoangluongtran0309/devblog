package com.hoangluongtran0309.devblog.content.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidProjectStatusException extends BusinessException {

    public InvalidProjectStatusException(ProjectId projectId, ContentStatus current) {
        super("Project %s has invalid status: %s"
                .formatted(projectId.asString(), current.name()));
    }
}