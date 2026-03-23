package com.hoangluongtran0309.devblog.infrastructure.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagService;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    private final CategoryService categoryService;
    private final TagService tagService;

    public GlobalModelAttributeAdvice(CategoryService categoryService, TagService tagService) {
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.getAll();
    }

    @ModelAttribute("tags")
    public Iterable<Tag> tags() {
        return tagService.getAll();
    }
}