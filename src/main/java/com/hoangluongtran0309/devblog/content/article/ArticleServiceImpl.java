package com.hoangluongtran0309.devblog.content.article;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.category.CategoryNotFoundException;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagService;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

        private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

        private final ArticleRepository articleRepository;
        private final CategoryService categoryService;
        private final TagService tagService;

        public ArticleServiceImpl(
                        ArticleRepository articleRepository,
                        CategoryService categoryService,
                        TagService tagService) {
                this.articleRepository = articleRepository;
                this.categoryService = categoryService;
                this.tagService = tagService;
        }

        @Override
        @Transactional(readOnly = true)
        public boolean existsBySlug(Slug articleSlug) {
                return articleRepository.existsByArticleSlug(articleSlug);
        }

        @Override
        @Transactional(readOnly = true)
        public boolean existsByTitle(ArticleTitle articleTitle) {
                return articleRepository.existsByArticleTitle(articleTitle);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<Article> getBySlug(Slug articleSlug) {
                return articleRepository.findByArticleSlug(articleSlug);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<Article> getById(ArticleId articleId) {
                return articleRepository.findById(articleId);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Article> getPublishedArticles(Pageable pageable) {
                return articleRepository
                                .findByContentStatusOrderByPublishedAtDesc(
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Article> getPublishedArticlesByCategory(Slug categorySlug, Pageable pageable) {
                return articleRepository
                                .findByCategory_CategorySlugAndContentStatusOrderByPublishedAtDesc(
                                                categorySlug,
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Article> getPublishedArticlesByTag(Slug tagSlug, Pageable pageable) {
                return articleRepository
                                .findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
                                                tagSlug,
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Article> searchPublishedArticles(String keyword, Pageable pageable) {
                return articleRepository
                                .findByArticleTitle_ArticleTitleContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
                                                keyword,
                                                ContentStatus.PUBLISHED,
                                                pageable);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<Article> getAll(Pageable pageable) {
                return articleRepository.findAll(pageable);
        }

        @Override
        public Article create(CreateArticleParameters parameters) {
                logger.info("Creating draft article with title: {}", parameters.getArticleTitle());

                validateForCreate(parameters);

                Category category = getCategory(parameters.getCategoryId());
                Set<Tag> tags = getTags(parameters.getTagIds());

                Article article = new Article(
                                articleRepository.nextId(),
                                parameters.getArticleTitle(),
                                parameters.getArticleSummary(),
                                parameters.getArticleContent(),
                                parameters.getImagePreview(),
                                parameters.getArticleSlug(),
                                ContentStatus.DRAFT,
                                category,
                                tags);

                logger.info("Draft article created with ID: {}", article.getId().asString());

                return articleRepository.save(article);
        }

        @Override
        public Article update(ArticleId articleId, UpdateArticleParameters parameters) {
                logger.info("Updating article with ID: {}", articleId.asString());

                Article article = getById(articleId)
                                .orElseThrow(() -> new ArticleNotFoundException(articleId));

                if (!isDraftArticle(article)) {
                        throw new InvalidArticleStatusException(articleId, article.getContentStatus());
                }

                validateForUpdate(article, parameters);

                Category category = getCategory(parameters.getCategoryId());
                Set<Tag> tags = getTags(parameters.getTagIds());

                article.setArticleTitle(parameters.getArticleTitle());
                article.setArticleSummary(parameters.getArticleSummary());
                article.setArticleContent(parameters.getArticleContent());
                article.setImagePreview(parameters.getImagePreview());
                article.setArticleSlug(parameters.getArticleSlug());
                article.setCategory(category);
                article.setTags(tags);

                logger.info("Article updated with ID: {}", articleId.asString());

                return articleRepository.save(article);
        }

        @Override
        public Article publish(ArticleId articleId) {
                logger.info("Publishing article with ID: {}", articleId.asString());

                Article article = getById(articleId)
                                .orElseThrow(() -> new ArticleNotFoundException(articleId));

                if (!isDraftArticle(article)) {
                        throw new InvalidArticleStatusException(articleId, article.getContentStatus());
                }

                article.setContentStatus(ContentStatus.PUBLISHED);
                article.setPublishedAt(LocalDateTime.now());

                logger.info("Article published with ID: {}", articleId.asString());

                return articleRepository.save(article);
        }

        @Override
        public void delete(ArticleId articleId) {
                logger.info("Deleting article with ID: {}", articleId.asString());

                Article article = getById(articleId)
                                .orElseThrow(() -> new ArticleNotFoundException(articleId));

                articleRepository.delete(article);

                logger.info("Article deleted with ID: {}", articleId.asString());
        }

        private Category getCategory(CategoryId categoryId) {
                return categoryService.getById(categoryId)
                                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        }

        private Set<Tag> getTags(Set<TagId> tagIds) {
                return StreamSupport.stream(tagService.getAllById(tagIds).spliterator(), false)
                                .collect(Collectors.toSet());
        }

        private boolean isDraftArticle(Article article) {
                return article.getContentStatus() == ContentStatus.DRAFT;
        }

        private void validateForCreate(CreateArticleParameters parameters) {
                if (existsBySlug(parameters.getArticleSlug())) {
                        throw new ArticleAlreadyExistsException(parameters.getArticleSlug());
                }

                if (existsByTitle(parameters.getArticleTitle())) {
                        throw new ArticleAlreadyExistsException(parameters.getArticleTitle());
                }
        }

        private void validateForUpdate(Article article, UpdateArticleParameters parameters) {
                if (article.getVersion() != parameters.getVersion()) {
                        throw new ObjectOptimisticLockingFailureException(
                                        Article.class,
                                        article.getId().asString());
                }

                if (!article.getArticleSlug().equals(parameters.getArticleSlug())
                                && existsBySlug(parameters.getArticleSlug())) {
                        throw new ArticleAlreadyExistsException(parameters.getArticleSlug());
                }

                if (!article.getArticleTitle().equals(parameters.getArticleTitle())
                                && existsByTitle(parameters.getArticleTitle())) {
                        throw new ArticleAlreadyExistsException(parameters.getArticleTitle());
                }
        }
}