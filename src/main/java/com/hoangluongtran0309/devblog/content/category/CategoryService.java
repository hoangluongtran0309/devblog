package com.hoangluongtran0309.devblog.content.category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public interface CategoryService {

    boolean existsBySlug(Slug categorySlug);

    boolean existsByName(CategoryName categoryName);

    Optional<Category> getBySlug(Slug categorySlug);

    Optional<Category> getById(CategoryId categoryId);

    Page<Category> getAll(Pageable pageable);

    Iterable<Category> getAll();

    Category create(CategoryParameters parameters);

    Category update(CategoryId categoryId, CategoryParameters parameters);

    void delete(CategoryId categoryId);
}