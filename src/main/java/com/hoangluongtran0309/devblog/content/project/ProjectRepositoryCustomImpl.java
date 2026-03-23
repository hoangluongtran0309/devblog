package com.hoangluongtran0309.devblog.content.project;

public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    @Override
    public ProjectId nextId() {
        return ProjectId.generate();
    }
}
