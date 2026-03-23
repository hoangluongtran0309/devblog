package com.hoangluongtran0309.devblog.content.tag.web;

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

import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagId;
import com.hoangluongtran0309.devblog.content.tag.TagNotFoundException;
import com.hoangluongtran0309.devblog.content.tag.TagService;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.web.EditMode;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/tags")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTagController {

    private final TagService tagService;

    public AdminTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String listTags(@PageableDefault(size = 10, sort = "tagName") Pageable pageable, Model model) {
        Page<Tag> tags = tagService.getAll(pageable);
        model.addAttribute("tags", tags);
        return "admin/tag/list";
    }

    @GetMapping("/new")
    public String createTagForm(Model model) {
        model.addAttribute("createTagForm", new CreateTagFormData());
        model.addAttribute("editMode", EditMode.CREATE);
        return "admin/tag/form";
    }

    @PostMapping("/new")
    public String doCreateTag(
            @Valid @ModelAttribute("createTagForm") CreateTagFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            return "admin/tag/form";
        }

        try {
            tagService.create(formData.toParameters());
            return "redirect:/admin/tags";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/tag/form";
        }
    }

    @GetMapping("/{id}/update")
    public String updateTagForm(@PathVariable("id") TagId id, Model model) {
        Tag tag = tagService.getById(id).orElseThrow(() -> new TagNotFoundException(id));
        model.addAttribute("updateTagForm", UpdateTagFormData.fromData(tag));
        model.addAttribute("editMode", EditMode.UPDATE);
        return "admin/tag/form";
    }

    @PostMapping("/{id}/update")
    public String doUpdateTag(
            @PathVariable("id") TagId id,
            @Valid @ModelAttribute("updateTagForm") UpdateTagFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "admin/tag/form";
        }

        try {
            tagService.update(id, formData.toParameters());
            return "redirect:/admin/tags";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/tag/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String doDeleteTag(@PathVariable("id") TagId id, RedirectAttributes redirectAttributes) {
        Tag tag = tagService.getById(id).orElseThrow(() -> new TagNotFoundException(id));
        tagService.delete(id);
        redirectAttributes.addFlashAttribute("deletedTagName", tag.getTagName().asString());
        return "redirect:/admin/tags";
    }
}