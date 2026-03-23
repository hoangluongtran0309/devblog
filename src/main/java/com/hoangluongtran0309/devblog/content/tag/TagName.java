package com.hoangluongtran0309.devblog.content.tag;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class TagName {

    private String tagName;

    protected TagName() {

    }

    public TagName(String tagName) {
        Assert.hasText(tagName, "tagName cannot be blank");
        this.tagName = tagName;
    }

    public String asString() {
        return tagName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
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
        TagName other = (TagName) obj;
        if (tagName == null) {
            if (other.tagName != null)
                return false;
        } else if (!tagName.equals(other.tagName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tagName", tagName)
                .toString();
    }
}
