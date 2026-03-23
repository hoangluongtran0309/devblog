package com.hoangluongtran0309.devblog.content.article;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.category.CategoryName;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.infrastructure.persistence.JpaConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ JpaConfiguration.class })
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private ArticleId articleId;
    private ArticleTitle articleTitle;
    private ArticleSummary articleSummary;
    private ArticleContent articleContent;
    private ImagePreview imagePreview;
    private Slug articleSlug;
    private Category category;
    private Tag tag;

    @BeforeEach
    void setUp() {
        articleId = ArticleId.generate();
        articleTitle = new ArticleTitle("Spring Boot DDD");
        articleSummary = new ArticleSummary("Spring Boot DDD Summary");
        articleContent = new ArticleContent("Spring Boot DDD Content");
        imagePreview = new ImagePreview("https://www.example.com/preview.png");
        articleSlug = new Slug("spring-boot-ddd");

        CategoryId categoryId = CategoryId.generate();
        category = new Category(
                categoryId,
                new CategoryName("Backend"),
                "icon",
                new Slug("backend"));

        TagId tagId = TagId.generate();
        tag = new Tag(
                tagId,
                new TagName("Spring"),
                new Slug("spring"));

        entityManager.persist(category);
        entityManager.persist(tag);

        assertThat(articleRepository.count()).isZero();
    }

    private Article createArticle() {
        return new Article(
                articleId,
                articleTitle,
                articleSummary,
                articleContent,
                imagePreview,
                articleSlug,
                ContentStatus.PUBLISHED,
                category,
                Set.of(tag));
    }

    @Test
    void testSaveArticle() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_article", UUID.class))
                .isEqualTo(articleId.asString());

        assertThat(jdbcTemplate.queryForObject("SELECT article_slug FROM db_article", String.class))
                .isEqualTo(articleSlug.asString());
    }

    @Test
    void testExistsBySlug() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        assertThat(articleRepository.existsByArticleSlug(articleSlug)).isTrue();
    }

    @Test
    void testExistsByTitle() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        assertThat(articleRepository.existsByArticleTitle(articleTitle)).isTrue();
    }

    @Test
    void testFindBySlug() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        assertThat(articleRepository.findByArticleSlug(articleSlug)).isPresent();
    }

    @Test
    void testFindPublishedArticles() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        var page = articleRepository.findByContentStatusOrderByPublishedAtDesc(
                ContentStatus.PUBLISHED,
                PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testFindByCategorySlug() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        var page = articleRepository.findByCategory_CategorySlugAndContentStatusOrderByPublishedAtDesc(
                new Slug("backend"),
                ContentStatus.PUBLISHED,
                PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testFindByTagSlug() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        var page = articleRepository.findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
                new Slug("spring"),
                ContentStatus.PUBLISHED,
                PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testSearchByTitle() {
        Article article = createArticle();

        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        var page = articleRepository
                .findByArticleTitle_ArticleTitleContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
                        "spring",
                        ContentStatus.PUBLISHED,
                        PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }
}