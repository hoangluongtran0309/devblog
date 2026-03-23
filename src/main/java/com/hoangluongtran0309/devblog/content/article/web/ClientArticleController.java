package com.hoangluongtran0309.devblog.content.article.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.article.ArticleNotFoundException;
import com.hoangluongtran0309.devblog.content.article.ArticleService;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagService;

@Controller
@RequestMapping("/articles")
public class ClientArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public ClientArticleController(ArticleService articleService,
            CategoryService categoryService,
            TagService tagService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping
    public String listPublishedArticles(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Article> articles = articleService.getPublishedArticles(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "client/article/list";
    }

    @GetMapping("/{slug}")
    public String viewArticle(@PathVariable("slug") Slug articleSlug, Model model) {
        Article article = articleService.getBySlug(articleSlug)
                .orElseThrow(() -> new ArticleNotFoundException(articleSlug));
        model.addAttribute("article", article);
        return "client/article/detail";
    }

    @GetMapping("/category/{categorySlug}")
    public String listByCategory(
            @PathVariable("categorySlug") Slug categorySlug,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Article> articles = articleService.getPublishedArticlesByCategory(categorySlug, pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("currentCategorySlug", categorySlug);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "client/article/list";
    }

    @GetMapping("/tag/{tagSlug}")
    public String listByTag(
            @PathVariable("tagSlug") Slug tagSlug,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Article> articles = articleService.getPublishedArticlesByTag(tagSlug, pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("currentTagSlug", tagSlug);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "client/article/list";
    }

    @GetMapping("/search")
    public String searchArticles(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Article> articles = articleService.searchPublishedArticles(keyword, pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "client/article/list";
    }
}