package com.hoangluongtran0309.devblog.content.project.web;

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

import com.hoangluongtran0309.devblog.content.project.Project;
import com.hoangluongtran0309.devblog.content.project.ProjectId;
import com.hoangluongtran0309.devblog.content.project.ProjectNotFoundException;
import com.hoangluongtran0309.devblog.content.project.ProjectService;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.web.EditMode;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/projects")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProjectController {

    private final ProjectService projectService;
    private final TagService tagService;

    public AdminProjectController(ProjectService projectService, TagService tagService) {
        this.projectService = projectService;
        this.tagService = tagService;
    }

    @GetMapping
    public String listProjects(@PageableDefault(size = 10, sort = "publishedAt") Pageable pageable, Model model) {
        Page<Project> projects = projectService.getAll(pageable);
        model.addAttribute("projects", projects);
        return "admin/project/list";
    }

    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("createProjectForm", new CreateProjectFormData());
        model.addAttribute("editMode", EditMode.CREATE);
        model.addAttribute("tags", tagService.getAll());
        return "admin/project/form";
    }

    @PostMapping("/new")
    public String doCreateProject(
            @Valid @ModelAttribute("createProjectForm") CreateProjectFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("tags", tagService.getAll());
            return "admin/project/form";
        }

        try {
            projectService.create(formData.toParameters());
            return "redirect:/admin/projects";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("tags", tagService.getAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/project/form";
        }
    }

    @GetMapping("/{id}/update")
    public String updateProjectForm(@PathVariable("id") ProjectId id, Model model) {
        Project project = projectService.getById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        model.addAttribute("updateProjectForm", UpdateProjectFormData.fromData(project));
        model.addAttribute("editMode", EditMode.UPDATE);
        model.addAttribute("tags", tagService.getAll());
        return "admin/project/form";
    }

    @PostMapping("/{id}/update")
    public String doUpdateProject(
            @PathVariable("id") ProjectId id,
            @Valid @ModelAttribute("updateProjectForm") UpdateProjectFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("tags", tagService.getAll());
            return "admin/project/form";
        }

        try {
            projectService.update(id, formData.toParameters());
            return "redirect:/admin/projects";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("tags", tagService.getAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/project/form";
        }
    }

    @PostMapping("/{id}/publish")
    public String doPublishProject(@PathVariable("id") ProjectId id, RedirectAttributes redirectAttributes) {
        Project project = projectService.getById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        try {
            projectService.publish(id);
            redirectAttributes.addFlashAttribute("publishedProjectName",
                    project.getProjectName().asString());
            return "redirect:/admin/projects";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/projects";
        }
    }

    @PostMapping("/{id}/delete")
    public String doDeleteProject(@PathVariable("id") ProjectId id, RedirectAttributes redirectAttributes) {
        Project project = projectService.getById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("deletedProjectName", project.getProjectName().asString());
        return "redirect:/admin/projects";
    }
}