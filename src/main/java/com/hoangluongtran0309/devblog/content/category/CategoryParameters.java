package com.hoangluongtran0309.devblog.content.category;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public class CategoryParameters {

    private final CategoryName categoryName;
    private final String categoryIcon;
    private final Slug categorySlug;

    public CategoryParameters(CategoryName categoryName, String categoryIcon, Slug categorySlug) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categorySlug = categorySlug;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public Slug getCategorySlug() {
        return categorySlug;
    }
}
