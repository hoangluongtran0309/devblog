package com.hoangluongtran0309.devblog.infrastructure.web;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.article.ArticleService;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String index(Model model) {
        List<Article> recentArticles = articleService
                .getPublishedArticles(PageRequest.of(0, 5))
                .getContent();
        model.addAttribute("recentArticles", recentArticles);
        return "client/index";
    }
}
