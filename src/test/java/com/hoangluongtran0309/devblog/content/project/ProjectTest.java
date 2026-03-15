package com.hoangluongtran0309.devblog.content.project;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;

class ProjectTest {

        private ProjectId projectId;
        private ProjectName name;
        private ProjectSummary summary;
        private ProjectContent content;
        private ImagePreview imagePreview;
        private Slug slug;
        private Set<Tag> tags;

        @BeforeEach
        void setUp() {
                projectId = new ProjectId();
                name = new ProjectName("Dev Blog");
                summary = new ProjectSummary("Personal developer blog");
                content = new ProjectContent("Project description");
                imagePreview = new ImagePreview("https://www.example.com/preview.png");
                slug = new Slug("dev-blog");
                tags = new HashSet<>();
        }

        @Test
        void shouldCreateDraftProject() {

                LocalDateTime publishTime = LocalDateTime.now();

                Project project = Project.createDraft(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                publishTime,
                                tags);

                assertThat(project.getContentStatus()).isEqualTo(ContentStatus.DRAFT);
                assertThat(project.getProjectName()).isEqualTo(name);
                assertThat(project.getPublishedAt()).isEqualTo(publishTime);
        }

        @Test
        void shouldCreatePublishedProject() {

                Project project = Project.createPublished(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                tags);

                assertThat(project.getContentStatus()).isEqualTo(ContentStatus.PUBLISHED);
                assertThat(project.getPublishedAt()).isNotNull();
        }

        @Test
        void draftProjectShouldBeUpdated() {

                Project project = Project.createDraft(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                tags);

                ProjectName newName = new ProjectName("New Dev Blog");

                project.update(
                                newName,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                tags);

                assertThat(project.getProjectName()).isEqualTo(newName);
        }

        @Test
        void publishedProjectShouldNotBeUpdated() {

                Project project = Project.createPublished(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                tags);

                assertThatThrownBy(() -> project.update(
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                tags))
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("Only draft projects can be updated");
        }

        @Test
        void draftProjectShouldBePublished() {

                Project project = Project.createDraft(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                LocalDateTime.now(),
                                tags);

                LocalDateTime publishTime = LocalDateTime.now();

                project.publish(publishTime);

                assertThat(project.getContentStatus()).isEqualTo(ContentStatus.PUBLISHED);
                assertThat(project.getPublishedAt()).isEqualTo(publishTime);
        }

        @Test
        void publishedProjectShouldNotBePublishedAgain() {

                Project project = Project.createPublished(
                                projectId,
                                name,
                                summary,
                                content,
                                imagePreview,
                                slug,
                                tags);

                assertThatThrownBy(() -> project.publish(LocalDateTime.now()))
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("Only draft projects can be published");
        }
}