package com.hoangluongtran0309.devblog.content.project.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hoangluongtran0309.devblog.content.project.Project;
import com.hoangluongtran0309.devblog.content.project.ProjectNotFoundException;
import com.hoangluongtran0309.devblog.content.project.ProjectService;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagService;

@Controller
@RequestMapping("/projects")
public class ClientProjectController {

    private final ProjectService projectService;
    private final TagService tagService;

    public ClientProjectController(ProjectService projectService, TagService tagService) {
        this.projectService = projectService;
        this.tagService = tagService;
    }

    @GetMapping
    public String listPublishedProjects(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Project> projects = projectService.getPublishedProjects(pageable);
        model.addAttribute("projects", projects);
        model.addAttribute("tags", tagService.getAll());
        return "client/project/list";
    }

    @GetMapping("/{slug}")
    public String viewProject(@PathVariable("slug") Slug projectSlug, Model model) {
        Project project = projectService.getBySlug(projectSlug)
                .orElseThrow(() -> new ProjectNotFoundException(projectSlug));
        model.addAttribute("project", project);
        return "client/project/detail";
    }

    @GetMapping("/tag/{tagSlug}")
    public String listByTag(
            @PathVariable("tagSlug") Slug tagSlug,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Project> projects = projectService.getPublishedProjectsByTag(tagSlug, pageable);
        model.addAttribute("projects", projects);
        model.addAttribute("currentTagSlug", tagSlug);
        model.addAttribute("tags", tagService.getAll());
        return "client/project/list";
    }

    @GetMapping("/search")
    public String searchProjects(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Project> projects = projectService.searchPublishedProjects(keyword, pageable);
        model.addAttribute("projects", projects);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tags", tagService.getAll());
        return "client/project/list";
    }
}