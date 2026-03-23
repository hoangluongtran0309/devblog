package com.hoangluongtran0309.devblog.content.tag;

import java.util.List;
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
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private TagId tagId;
    private TagName tagName;
    private Slug tagSlug;

    @BeforeEach
    void setUp() {
        tagId = TagId.generate();
        tagName = new TagName("Spring");
        tagSlug = new Slug("spring");

        assertThat(tagRepository.count()).isZero();
    }

    private Tag createTag() {
        return new Tag(
                tagId,
                tagName,
                tagSlug);
    }

    @Test
    void testSaveTag() {
        Tag tag = createTag();

        tagRepository.save(tag);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_tag", UUID.class))
                .isEqualTo(tagId.asString());

        assertThat(jdbcTemplate.queryForObject("SELECT tag_slug FROM db_tag", String.class))
                .isEqualTo(tagSlug.asString());
    }

    @Test
    void testExistsBySlug() {
        Tag tag = createTag();

        tagRepository.save(tag);
        entityManager.flush();
        entityManager.clear();

        assertThat(tagRepository.existsByTagSlug(tagSlug)).isTrue();
    }

    @Test
    void testExistsByName() {
        Tag tag = createTag();

        tagRepository.save(tag);
        entityManager.flush();
        entityManager.clear();

        assertThat(tagRepository.existsByTagName(tagName)).isTrue();
    }

    @Test
    void testFindBySlug() {
        Tag tag = createTag();

        tagRepository.save(tag);
        entityManager.flush();
        entityManager.clear();

        assertThat(tagRepository.findByTagSlug(tagSlug)).isPresent();
    }

    @Test
    void testFindAllById() {
        Tag tag1 = new Tag(
                TagId.generate(),
                new TagName("Spring"),
                new Slug("spring"));

        Tag tag2 = new Tag(
                TagId.generate(),
                new TagName("Java"),
                new Slug("java"));

        tagRepository.save(tag1);
        tagRepository.save(tag2);

        entityManager.flush();
        entityManager.clear();

        Iterable<Tag> result = tagRepository.findAllById(List.of(tag1.getId(), tag2.getId()));

        assertThat(result).hasSize(2).extracting(Tag::getId)
                .containsExactlyInAnyOrder(tag1.getId(), tag2.getId());
    }
}