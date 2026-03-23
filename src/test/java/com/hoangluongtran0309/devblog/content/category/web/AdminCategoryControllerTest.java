package com.hoangluongtran0309.devblog.content.category.web;

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

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.category.CategoryName;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.config.TestConfig;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;
import com.hoangluongtran0309.devblog.infrastructure.web.GlobalModelAttributeAdvice;

@WebMvcTest(controllers = AdminCategoryController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalModelAttributeAdvice.class))
@Import(TestConfig.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private CategoryId categoryId;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryId = CategoryId.generate();
        category = new Category(categoryId, new CategoryName("Backend"), "icon", new Slug("backend"));
    }

    @Test
    void listCategoriesRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void getCreateCategoryForm() throws Exception {
        mockMvc.perform(get("/admin/categories/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().attributeExists("createCategoryForm", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateCategorySuccess() throws Exception {
        when(categoryService.create(any())).thenReturn(category);

        mockMvc.perform(post("/admin/categories/new")
                .with(csrf())
                .param("categoryName", "Backend")
                .param("categoryIcon", "icon")
                .param("categorySlug", "backend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateCategoryFailValidation() throws Exception {
        mockMvc.perform(post("/admin/categories/new")
                .with(csrf())
                .param("categoryName", "")
                .param("categoryIcon", "icon")
                .param("categorySlug", "backend"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().attributeExists("createCategoryForm", "editMode"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doCreateCategoryBusinessException() throws Exception {
        doThrow(new BusinessException("Duplicate category")).when(categoryService).create(any());

        mockMvc.perform(post("/admin/categories/new")
                .with(csrf())
                .param("categoryName", "Backend")
                .param("categoryIcon", "icon")
                .param("categorySlug", "backend"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().attributeExists("editMode", "errorMessage"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doUpdateCategorySuccess() throws Exception {
        when(categoryService.getById(eq(categoryId))).thenReturn(Optional.of(category));

        mockMvc.perform(post("/admin/categories/" + categoryId.asString() + "/update")
                .with(csrf())
                .param("categoryName", "Backend Updated")
                .param("categoryIcon", "icon")
                .param("categorySlug", "backend-updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doDeleteCategorySuccess() throws Exception {
        when(categoryService.getById(eq(categoryId))).thenReturn(Optional.of(category));
        doNothing().when(categoryService).delete(eq(categoryId));

        mockMvc.perform(post("/admin/categories/" + categoryId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }
}