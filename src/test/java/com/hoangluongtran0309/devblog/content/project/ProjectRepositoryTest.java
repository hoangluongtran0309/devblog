package com.hoangluongtran0309.devblog.content.project;

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
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private ProjectId projectId;
    private ProjectName projectName;
    private ProjectSummary projectSummary;
    private ProjectContent projectContent;
    private ImagePreview imagePreview;
    private Slug projectSlug;
    private Tag tag;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        projectName = new ProjectName("Spring Boot Project");
        projectSummary = new ProjectSummary("Spring Boot Project Summary");
        projectContent = new ProjectContent("Spring Boot Project Content");
        imagePreview = new ImagePreview("https://www.example.com/preview.png");
        projectSlug = new Slug("spring-boot-project");

        TagId tagId = TagId.generate();
        tag = new Tag(
                tagId,
                new TagName("Spring"),
                new Slug("spring"));

        entityManager.persist(tag);

        assertThat(projectRepository.count()).isZero();
    }

    private Project createProject() {
        return new Project(
                projectId,
                projectName,
                projectSummary,
                projectContent,
                imagePreview,
                projectSlug,
                ContentStatus.PUBLISHED,
                Set.of(tag));
    }

    @Test
    void testSaveProject() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_project", UUID.class))
                .isEqualTo(projectId.asString());

        assertThat(jdbcTemplate.queryForObject("SELECT project_slug FROM db_project", String.class))
                .isEqualTo(projectSlug.asString());
    }

    @Test
    void testExistsBySlug() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        assertThat(projectRepository.existsByProjectSlug(projectSlug)).isTrue();
    }

    @Test
    void testExistsByName() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        assertThat(projectRepository.existsByProjectName(projectName)).isTrue();
    }

    @Test
    void testFindBySlug() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        assertThat(projectRepository.findByProjectSlug(projectSlug)).isPresent();
    }

    @Test
    void testFindPublishedProjects() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        var page = projectRepository
                .findByContentStatusOrderByPublishedAtDesc(
                        ContentStatus.PUBLISHED,
                        PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testFindByTagSlug() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        var page = projectRepository
                .findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
                        new Slug("spring"),
                        ContentStatus.PUBLISHED,
                        PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testSearchByName() {
        Project project = createProject();

        projectRepository.save(project);
        entityManager.flush();
        entityManager.clear();

        var page = projectRepository
                .findByProjectName_ProjectNameContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
                        "spring",
                        ContentStatus.PUBLISHED,
                        PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }
}