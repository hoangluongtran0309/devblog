package com.hoangluongtran0309.devblog.content.article.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.article.ArticleId;
import com.hoangluongtran0309.devblog.content.article.ArticleNotFoundException;
import com.hoangluongtran0309.devblog.content.article.ArticleService;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.web.EditMode;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/articles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public AdminArticleController(ArticleService articleService, CategoryService categoryService,
            TagService tagService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping
    public String listArticles(@PageableDefault(size = 10, sort = "publishedAt") Pageable pageable, Model model) {
        Page<Article> articles = articleService.getAll(pageable);
        model.addAttribute("articles", articles);
        return "admin/article/list";
    }

    @GetMapping("/new")
    public String createArticleForm(Model model) {
        model.addAttribute("createArticleForm", new CreateArticleFormData());
        model.addAttribute("editMode", EditMode.CREATE);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "admin/article/form";
    }

    @PostMapping("/new")
    public String doCreateArticle(
            @Valid @ModelAttribute("createArticleForm") CreateArticleFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("tags", tagService.getAll());
            return "admin/article/form";
        }

        try {
            articleService.create(formData.toParameters());
            return "redirect:/admin/articles";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("tags", tagService.getAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/article/form";
        }
    }

    @GetMapping("/{id}/update")
    public String updateArticleForm(@PathVariable("id") ArticleId id, Model model) {
        Article article = articleService.getById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        model.addAttribute("updateArticleForm", UpdateArticleFormData.fromData(article));
        model.addAttribute("editMode", EditMode.UPDATE);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "admin/article/form";
    }

    @PostMapping("/{id}/update")
    public String doUpdateArticle(
            @PathVariable("id") ArticleId id,
            @Valid @ModelAttribute("updateArticleForm") UpdateArticleFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("tags", tagService.getAll());
            return "admin/article/form";
        }

        try {
            articleService.update(id, formData.toParameters());
            return "redirect:/admin/articles";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("tags", tagService.getAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/article/form";
        }
    }

    @PostMapping("/{id}/publish")
    public String doPublishArticle(@PathVariable("id") ArticleId id, RedirectAttributes redirectAttributes) {
        Article article = articleService.getById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
                
        try {
            articleService.publish(id);
            redirectAttributes.addFlashAttribute("publishedArticleTitle",
                    article.getArticleTitle().asString());
            return "redirect:/admin/articles";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/articles";
        }
    }

    @PostMapping("/{id}/delete")
    public String doDeleteArticle(@PathVariable("id") ArticleId id, RedirectAttributes redirectAttributes) {
        Article article = articleService.getById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        articleService.delete(id);
        redirectAttributes.addFlashAttribute("deletedArticleTitle", article.getArticleTitle().asString());
        return "redirect:/admin/articles";
    }
}