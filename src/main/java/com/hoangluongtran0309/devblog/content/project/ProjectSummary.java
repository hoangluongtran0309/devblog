package com.hoangluongtran0309.devblog.content.project;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProjectSummary {

    private String projectSummary;

    protected ProjectSummary() {

    }

    public ProjectSummary(String projectSummary) {
        Assert.hasText(projectSummary, "Project summary cannot be blank");
        this.projectSummary = projectSummary;
    }

    public String asString() {
        return projectSummary;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectSummary == null) ? 0 : projectSummary.hashCode());
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
        ProjectSummary other = (ProjectSummary) obj;
        if (projectSummary == null) {
            if (other.projectSummary != null)
                return false;
        } else if (!projectSummary.equals(other.projectSummary))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("projectSummary", projectSummary)
                .toString();
    }
}
