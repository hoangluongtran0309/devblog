package com.hoangluongtran0309.devblog.content.project;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProjectContent {

    private String projectContent;

    protected ProjectContent() {

    }

    public ProjectContent(String projectContent) {
        Assert.hasText(projectContent, "Project content cannot be blank");
        this.projectContent = projectContent;
    }

    public String asString() {
        return projectContent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectContent == null) ? 0 : projectContent.hashCode());
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
        ProjectContent other = (ProjectContent) obj;
        if (projectContent == null) {
            if (other.projectContent != null)
                return false;
        } else if (!projectContent.equals(other.projectContent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("projectContent", projectContent)
                .toString();
    }
}
