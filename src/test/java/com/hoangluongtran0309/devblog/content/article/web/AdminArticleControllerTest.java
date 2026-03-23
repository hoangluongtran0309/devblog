package com.hoangluongtran0309.devblog.content.article.web;

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

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.article.ArticleContent;
import com.hoangluongtran0309.devblog.content.article.ArticleId;
import com.hoangluongtran0309.devblog.content.article.ArticleService;
import com.hoangluongtran0309.devblog.content.article.ArticleSummary;
import com.hoangluongtran0309.devblog.content.article.ArticleTitle;
import com.hoangluongtran0309.devblog.content.article.CreateArticleParameters;
import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.category.CategoryName;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.config.TestConfig;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;

@WebMvcTest(AdminArticleController.class)
@Import(TestConfig.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminArticleControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ArticleService articleService;

        @MockitoBean
        private CategoryService categoryService;

        @MockitoBean
        private TagService tagService;

        private ArticleId articleId;
        private Article article;
        private Category category;
        private Tag tag;

        @BeforeEach
        void setUp() {
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

                articleId = ArticleId.generate();
                article = new Article(
                                articleId,
                                new ArticleTitle("Spring Boot DDD"),
                                new ArticleSummary("Spring Boot DDD Summary"),
                                new ArticleContent("Spring Boot DDD Content"),
                                new ImagePreview("https://www.example.com/preview.png"),
                                new Slug("spring-boot-ddd"),
                                ContentStatus.DRAFT,
                                category,
                                Set.of(tag));
        }

        @Test
        void listArticlesRedirectToLoginWhenNotAuthenticated() throws Exception {
                mockMvc.perform(get("/admin/articles"))
                                .andExpect(status().is3xxRedirection());
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void getCreateArticleForm() throws Exception {
                when(categoryService.getAll()).thenReturn(List.of(category));
                when(tagService.getAll()).thenReturn(List.of(tag));

                mockMvc.perform(get("/admin/articles/new"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("admin/article/form"))
                                .andExpect(model().attributeExists("createArticleForm", "categories", "tags",
                                                "editMode"));
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void doCreateArticleSuccess() throws Exception {
                when(categoryService.getAll()).thenReturn(List.of(category));
                when(tagService.getAll()).thenReturn(List.of(tag));
                when(articleService.create(any(CreateArticleParameters.class))).thenReturn(article);

                mockMvc.perform(post("/admin/articles/new")
                                .with(csrf())
                                .param("articleTitle", "Spring Boot DDD")
                                .param("articleSummary", "Spring Boot DDD Summary")
                                .param("articleContent", "Spring Boot DDD Content")
                                .param("imagePreview", "https://www.example.com/preview.png")
                                .param("articleSlug", "spring-boot-ddd")
                                .param("categoryId", category.getId().asString().toString())
                                .param("tagIds", tag.getId().asString().toString()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/admin/articles"));
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void doCreateArticleFailValidation() throws Exception {
                when(categoryService.getAll()).thenReturn(List.of(category));
                when(tagService.getAll()).thenReturn(List.of(tag));

                mockMvc.perform(post("/admin/articles/new")
                                .with(csrf())
                                .param("articleTitle", "") // blank title triggers validation
                                .param("articleSummary", "Spring Boot DDD Summary")
                                .param("articleContent", "Spring Boot DDD Content")
                                .param("articleSlug", "t")
                                .param("categoryId", category.getId().asString().toString())
                                .param("tagIds", tag.getId().asString().toString()))
                                .andExpect(status().isOk())
                                .andExpect(view().name("admin/article/form"))
                                .andExpect(model().attributeExists("createArticleForm", "categories", "tags",
                                                "editMode"));
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void doUpdateArticleSuccess() throws Exception {
                when(articleService.getById(eq(articleId))).thenReturn(Optional.of(article));
                when(categoryService.getAll()).thenReturn(List.of(category));
                when(tagService.getAll()).thenReturn(List.of(tag));

                mockMvc.perform(post("/admin/articles/" + articleId.asString() + "/update")
                                .with(csrf())
                                .param("articleTitle", "Spring Boot DDD Updated")
                                .param("articleSummary", "Spring Boot DDD Summary Updated")
                                .param("articleContent", "Spring Boot DDD Content Updated")
                                .param("imagePreview", "https://www.example.com/preview.png")
                                .param("articleSlug", "spring-boot-ddd-updated")
                                .param("categoryId", category.getId().asString().toString())
                                .param("tagIds", tag.getId().asString().toString())
                                .param("version", String.valueOf(article.getVersion())))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/admin/articles"));
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void doPublishArticleSuccess() throws Exception {
                when(articleService.getById(eq(articleId))).thenReturn(Optional.of(article));
                when(articleService.publish(eq(articleId))).thenReturn(article);

                mockMvc.perform(post("/admin/articles/" + articleId.asString() + "/publish")
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/admin/articles"));
        }

        @Test
        @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
        void doDeleteArticleSuccess() throws Exception {
                when(articleService.getById(eq(articleId))).thenReturn(Optional.of(article));
                doNothing().when(articleService).delete(eq(articleId));

                mockMvc.perform(post("/admin/articles/" + articleId.asString() + "/delete")
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/admin/articles"));
        }
}