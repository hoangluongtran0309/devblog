package com.hoangluongtran0309.devblog.content.category;

import java.util.HashSet;
import java.util.Set;

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "db_category")
public class Category extends BaseEntity<CategoryId> {

    @Embedded
    @NotNull
    private CategoryName categoryName;

    private String categoryIcon;

    @Embedded
    @AttributeOverride(name = "slug", column = @Column(name = "category_slug"))
    @NotNull
    private Slug categorySlug;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private final Set<Article> articles = new HashSet<>();

    protected Category() {

    }

    public Category(CategoryId id, CategoryName categoryName, String categoryIcon, Slug categorySlug) {
        super(id);
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categorySlug = categorySlug;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public Slug getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(Slug categorySlug) {
        this.categorySlug = categorySlug;
    }

    public Set<Article> getArticles() {
        return articles;
    }
}
