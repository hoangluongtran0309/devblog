package com.hoangluongtran0309.devblog.content.category;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    @Override
    public CategoryId nextId() {
        return CategoryId.generate();
    }
}
