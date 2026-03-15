package com.hoangluongtran0309.devblog.content.category;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoangluongtran0309.devblog.content.shared.Slug;

class CategoryTest {

    private CategoryId categoryId;
    private CategoryName categoryName;
    private Slug slug;
    private String icon;

    @BeforeEach
    void setUp() {
        categoryId = new CategoryId();
        categoryName = new CategoryName("Backend");
        slug = new Slug("backend");
        icon = "fa-server";
    }

    @Test
    void shouldCreateCategory() {

        Category category = new Category(
                categoryId,
                categoryName,
                icon,
                slug);

        assertThat(category.getCategoryName()).isEqualTo(categoryName);
        assertThat(category.getCategoryIcon()).isEqualTo(icon);
        assertThat(category.getCategorySlug()).isEqualTo(slug);
    }

    @Test
    void categoryShouldHaveEmptyArticlesInitially() {

        Category category = new Category(
                categoryId,
                categoryName,
                icon,
                slug);

        assertThat(category.getArticles()).isNotNull();
        assertThat(category.getArticles()).isEmpty();
    }
}