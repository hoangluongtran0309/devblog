package com.hoangluongtran0309.devblog.content.shared;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class Slug {

    private String slug;

    protected Slug() {

    }

    public Slug(String slug) {
        Assert.hasText(slug, "Slug cannot be blank");
        Assert.isTrue(slug.contains("-"), "Slug should contain - symbol");
        this.slug = slug;
    }

    public String asString() {
        return slug;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((slug == null) ? 0 : slug.hashCode());
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
        Slug other = (Slug) obj;
        if (slug == null) {
            if (other.slug != null)
                return false;
        } else if (!slug.equals(other.slug))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("slug", slug)
                .toString();
    }
}
