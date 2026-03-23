package com.hoangluongtran0309.devblog.content.tag.web;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.config.TestConfig;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;
import com.hoangluongtran0309.devblog.infrastructure.web.GlobalModelAttributeAdvice;

@WebMvcTest(controllers = AdminTagController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalModelAttributeAdvice.class))
@Import(TestConfig.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService tagService;

    private TagId tagId;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tagId = TagId.generate();
        tag = new Tag(tagId, new TagName("Spring"), new Slug("spring"));
    }

    @Test
    void listCategoriesRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/tags"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void getCreateTagForm() throws Exception {
        mockMvc.perform(get("/admin/tags/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tag/form"))
                .andExpect(model().attributeExists("createTagForm", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateTagSuccess() throws Exception {
        when(tagService.create(any())).thenReturn(tag);

        mockMvc.perform(post("/admin/tags/new")
                .with(csrf())
                .param("tagName", "Spring")

                .param("tagSlug", "spring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tags"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateTagFailValidation() throws Exception {
        mockMvc.perform(post("/admin/tags/new")
                .with(csrf())
                .param("tagName", "")

                .param("tagSlug", "spring"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tag/form"))
                .andExpect(model().attributeExists("createTagForm", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateTagBusinessException() throws Exception {
        doThrow(new BusinessException("Duplicate tag")).when(tagService).create(any());

        mockMvc.perform(post("/admin/tags/new")
                .with(csrf())
                .param("tagName", "Spring")

                .param("tagSlug", "spring"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tag/form"))
                .andExpect(model().attributeExists("editMode", "errorMessage"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doUpdateTagSuccess() throws Exception {
        when(tagService.getById(eq(tagId))).thenReturn(Optional.of(tag));

        mockMvc.perform(post("/admin/tags/" + tagId.asString() + "/update")
                .with(csrf())
                .param("tagName", "Spring Updated")

                .param("tagSlug", "spring-updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tags"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doDeleteTagSuccess() throws Exception {
        when(tagService.getById(eq(tagId))).thenReturn(Optional.of(tag));
        doNothing().when(tagService).delete(eq(tagId));

        mockMvc.perform(post("/admin/tags/" + tagId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tags"));
    }
}