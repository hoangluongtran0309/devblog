package com.hoangluongtran0309.devblog.content.article.web;

import com.hoangluongtran0309.devblog.content.article.ArticleContent;
import com.hoangluongtran0309.devblog.content.article.ArticleSummary;
import com.hoangluongtran0309.devblog.content.article.ArticleTitle;
import com.hoangluongtran0309.devblog.content.article.CreateArticleParameters;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;

public class CreateArticleFormData extends AbstractArticleFormData {

    public CreateArticleParameters toParameters() {
        return new CreateArticleParameters(
                new ArticleTitle(getArticleTitle()),
                new ArticleSummary(getArticleSummary()),
                new ArticleContent(getArticleContent()),
                new ImagePreview(getImagePreview()),
                new Slug(getArticleSlug()),
                getCategoryId(),
                getTagIds());
    }
}
