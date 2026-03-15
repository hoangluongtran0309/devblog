package com.hoangluongtran0309.devblog.content.article;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;

class ArticleTest {

        private ArticleId articleId;
        private ArticleTitle title;
        private ArticleSummary summary;
        private ArticleContent content;
        private ImagePreview imagePreview;
        private Slug slug;
        private Category category;
        private Set<Tag> tags;

        @BeforeEach
        void setUp() {
                articleId = new ArticleId();
                title = new ArticleTitle("Clean Architecture");
                summary = new ArticleSummary("Introduction to clean architecture");
                content = new ArticleContent("Content body");
                imagePreview = new ImagePreview("https://www.example.com/preview.png");
                slug = new Slug("clean-architecture");
                category = null;
                tags = new HashSet<>();
        }

        @Test
        void shouldCreateDraftArticle() {

                LocalDateTime publishTime = LocalDateTime.now();

                Article article = Article.createDraft(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                publishTime,
                                category,
                                tags);

                assertThat(article.getContentStatus()).isEqualTo(ContentStatus.DRAFT);
                assertThat(article.getArticleTitle()).isEqualTo(title);
                assertThat(article.getArticleSummary()).isEqualTo(summary);
                assertThat(article.getPublishedAt()).isEqualTo(publishTime);
        }

        @Test
        void shouldCreatePublishedArticle() {

                Article article = Article.createPublished(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                category,
                                tags);

                assertThat(article.getContentStatus()).isEqualTo(ContentStatus.PUBLISHED);
                assertThat(article.getPublishedAt()).isNotNull();
        }

        @Test
        void draftArticleShouldBeUpdated() {

                Article article = Article.createDraft(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                category,
                                tags);

                ArticleTitle newTitle = new ArticleTitle("DDD Architecture");

                article.update(
                                newTitle,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                category,
                                tags);

                assertThat(article.getArticleTitle()).isEqualTo(newTitle);
        }

        @Test
        void publishedArticleShouldNotBeUpdated() {

                Article article = Article.createPublished(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                category,
                                tags);

                assertThatThrownBy(() -> article.update(
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                category,
                                tags)).isInstanceOf(IllegalStateException.class)
                                .hasMessage("Only draft articles can be updated");
        }

        @Test
        void draftArticleShouldBePublished() {

                Article article = Article.createDraft(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                category,
                                tags);

                LocalDateTime publishTime = LocalDateTime.now();

                article.publish(publishTime);

                assertThat(article.getContentStatus()).isEqualTo(ContentStatus.PUBLISHED);
                assertThat(article.getPublishedAt()).isEqualTo(publishTime);
        }

        @Test
        void publishedArticleShouldNotBePublishedAgain() {

                Article article = Article.createPublished(
                                articleId,
                                title,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                category,
                                tags);

                assertThatThrownBy(() -> article.publish(LocalDateTime.now()))
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("Only draft articles can be published");
        }
}