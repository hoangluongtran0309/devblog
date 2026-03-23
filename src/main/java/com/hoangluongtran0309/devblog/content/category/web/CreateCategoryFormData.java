package com.hoangluongtran0309.devblog.content.category.web;

import com.hoangluongtran0309.devblog.content.category.CategoryName;
import com.hoangluongtran0309.devblog.content.category.CategoryParameters;
import com.hoangluongtran0309.devblog.content.shared.Slug;

public class CreateCategoryFormData extends AbstractCategoryFormData {

    public CategoryParameters toParameters() {
        return new CategoryParameters(
                new CategoryName(getCategoryName()),
                getCategoryIcon(),
                new Slug(getCategorySlug()));
    }
}
