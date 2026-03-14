package com.hoangluongtran0309.devblog.content.category;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CategoryName {

    private String categoryName;

    protected CategoryName() {

    }

    public CategoryName(String categoryName) {
        Assert.hasText(categoryName, "Category name cannot be blank");
        this.categoryName = categoryName;
    }

    public String asString() {
        return categoryName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
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
        CategoryName other = (CategoryName) obj;
        if (categoryName == null) {
            if (other.categoryName != null)
                return false;
        } else if (!categoryName.equals(other.categoryName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("categoryName", categoryName)
                .toString();
    }
}
