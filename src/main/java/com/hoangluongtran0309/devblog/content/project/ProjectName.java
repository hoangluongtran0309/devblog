package com.hoangluongtran0309.devblog.content.project;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProjectName {

    private String projectName;

    protected ProjectName() {

    }

    public ProjectName(String projectName) {
        Assert.hasText(projectName, "Project name cannot be blank");
        this.projectName = projectName;
    }

    public String asString() {
        return projectName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProjectName other = (ProjectName) obj;
        if (projectName == null) {
            if (other.projectName != null)
                return false;
        } else if (!projectName.equals(other.projectName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("projectName", projectName)
                .toString();
    }
}
