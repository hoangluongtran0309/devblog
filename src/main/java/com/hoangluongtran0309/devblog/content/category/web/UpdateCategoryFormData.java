package com.hoangluongtran0309.devblog.content.category.web;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryName;
import com.hoangluongtran0309.devblog.content.category.CategoryParameters;
import com.hoangluongtran0309.devblog.content.shared.Slug;

public class UpdateCategoryFormData extends AbstractCategoryFormData {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CategoryParameters toParameters() {
        return new CategoryParameters(
                new CategoryName(getCategoryName()),
                getCategoryIcon(),
                new Slug(getCategorySlug()));
    }

    public static UpdateCategoryFormData fromData(Category category) {
        UpdateCategoryFormData result = new UpdateCategoryFormData();

        result.setCategoryName(category.getCategoryName().asString());
        result.setCategoryIcon(category.getCategoryIcon());
        result.setCategorySlug(category.getCategorySlug().asString());

        return result;
    }
}
