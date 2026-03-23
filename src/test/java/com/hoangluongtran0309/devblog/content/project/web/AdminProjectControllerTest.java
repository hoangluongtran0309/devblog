package com.hoangluongtran0309.devblog.content.project.web;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hoangluongtran0309.devblog.content.project.CreateProjectParameters;
import com.hoangluongtran0309.devblog.content.project.Project;
import com.hoangluongtran0309.devblog.content.project.ProjectContent;
import com.hoangluongtran0309.devblog.content.project.ProjectId;
import com.hoangluongtran0309.devblog.content.project.ProjectName;
import com.hoangluongtran0309.devblog.content.project.ProjectService;
import com.hoangluongtran0309.devblog.content.project.ProjectSummary;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.config.TestConfig;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;
import com.hoangluongtran0309.devblog.infrastructure.web.GlobalModelAttributeAdvice;

@WebMvcTest(controllers = AdminProjectController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalModelAttributeAdvice.class))
@Import(TestConfig.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private TagService tagService;

    private ProjectId projectId;
    private Project project;
    private Tag tag;

    @BeforeEach
    void setUp() {
        TagId tagId = TagId.generate();
        tag = new Tag(
                tagId,
                new TagName("Spring"),
                new Slug("spring"));

        projectId = ProjectId.generate();
        project = new Project(
                projectId,
                new ProjectName("Spring Boot Project"),
                new ProjectSummary("Spring Boot Project Summary"),
                new ProjectContent("Spring Boot Project Content"),
                new ImagePreview("https://www.example.com/preview.png"),
                new Slug("spring-boot-project"),
                ContentStatus.DRAFT,
                Set.of(tag));
    }

    @Test
    void listProjectsRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/projects"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void getCreateProjectForm() throws Exception {
        when(tagService.getAll()).thenReturn(List.of(tag));

        mockMvc.perform(get("/admin/projects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/project/form"))
                .andExpect(model().attributeExists("createProjectForm", "tags", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateProjectSuccess() throws Exception {
        when(tagService.getAll()).thenReturn(List.of(tag));
        when(projectService.create(any(CreateProjectParameters.class))).thenReturn(project);

        mockMvc.perform(post("/admin/projects/new")
                .with(csrf())
                .param("projectName", "Spring Boot Project")
                .param("projectSummary", "Spring Boot Project Summary")
                .param("projectContent", "Spring Boot Project Content")
                .param("imagePreview", "https://www.example.com/preview.png")
                .param("projectSlug", "spring-boot-project")
                .param("tagIds", tag.getId().asString().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateProjectFailValidation() throws Exception {
        when(tagService.getAll()).thenReturn(List.of(tag));

        mockMvc.perform(post("/admin/projects/new")
                .with(csrf())
                .param("projectName", "") // blank title triggers validation
                .param("projectSummary", "Spring Boot Project Summary")
                .param("projectContent", "Spring Boot Project Content")
                .param("projectSlug", "t")
                .param("tagIds", tag.getId().asString().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/project/form"))
                .andExpect(model().attributeExists("createProjectForm", "tags", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doUpdateProjectSuccess() throws Exception {
        when(projectService.getById(eq(projectId))).thenReturn(Optional.of(project));
        when(tagService.getAll()).thenReturn(List.of(tag));

        mockMvc.perform(post("/admin/projects/" + projectId.asString() + "/update")
                .with(csrf())
                .param("projectName", "Spring Boot Project Updated")
                .param("projectSummary", "Spring Boot Project Summary Updated")
                .param("projectContent", "Spring Boot Project Content Updated")
                .param("imagePreview", "https://www.example.com/preview.png")
                .param("projectSlug", "spring-boot-project-updated")
                .param("tagIds", tag.getId().asString().toString())
                .param("version", String.valueOf(project.getVersion())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doPublishProjectSuccess() throws Exception {
        when(projectService.getById(eq(projectId))).thenReturn(Optional.of(project));
        when(projectService.publish(eq(projectId))).thenReturn(project);

        mockMvc.perform(post("/admin/projects/" + projectId.asString() + "/publish")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doDeleteProjectSuccess() throws Exception {
        when(projectService.getById(eq(projectId))).thenReturn(Optional.of(project));
        doNothing().when(projectService).delete(eq(projectId));

        mockMvc.perform(post("/admin/projects/" + projectId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }
}