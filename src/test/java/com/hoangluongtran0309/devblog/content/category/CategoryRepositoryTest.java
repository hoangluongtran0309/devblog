package com.hoangluongtran0309.devblog.content.category;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.persistence.JpaConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ JpaConfiguration.class })
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private CategoryId categoryId;
    private CategoryName categoryName;
    private Slug categorySlug;

    @BeforeEach
    void setUp() {
        categoryId = CategoryId.generate();
        categoryName = new CategoryName("Backend");
        categorySlug = new Slug("backend");

        assertThat(categoryRepository.count()).isZero();
    }

    private Category createCategory() {
        return new Category(
                categoryId,
                categoryName,
                "icon",
                categorySlug);
    }

    @Test
    void testSaveCategory() {
        Category category = createCategory();

        categoryRepository.save(category);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_category", UUID.class))
                .isEqualTo(categoryId.asString());

        assertThat(jdbcTemplate.queryForObject("SELECT category_slug FROM db_category", String.class))
                .isEqualTo(categorySlug.asString());
    }

    @Test
    void testExistsBySlug() {
        Category category = createCategory();

        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();

        assertThat(categoryRepository.existsByCategorySlug(categorySlug)).isTrue();
    }

    @Test
    void testExistsByName() {
        Category category = createCategory();

        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();

        assertThat(categoryRepository.existsByCategoryName(categoryName)).isTrue();
    }

    @Test
    void testFindBySlug() {
        Category category = createCategory();

        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();

        assertThat(categoryRepository.findByCategorySlug(categorySlug)).isPresent();
    }
}