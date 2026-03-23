package com.hoangluongtran0309.devblog.content.category.web;

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

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.category.CategoryNotFoundException;
import com.hoangluongtran0309.devblog.content.category.CategoryService;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.web.EditMode;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(@PageableDefault(size = 10, sort = "categoryName") Pageable pageable, Model model) {
        Page<Category> categories = categoryService.getAll(pageable);
        model.addAttribute("categories", categories);
        return "admin/category/list";
    }

    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        model.addAttribute("createCategoryForm", new CreateCategoryFormData());
        model.addAttribute("editMode", EditMode.CREATE);
        return "admin/category/form";
    }

    @PostMapping("/new")
    public String doCreateCategory(
            @Valid @ModelAttribute("createCategoryForm") CreateCategoryFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            return "admin/category/form";
        }

        try {
            categoryService.create(formData.toParameters());
            return "redirect:/admin/categories";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/category/form";
        }
    }

    @GetMapping("/{id}/update")
    public String updateCategoryForm(@PathVariable("id") CategoryId id, Model model) {
        Category category = categoryService.getById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        model.addAttribute("updateCategoryForm", UpdateCategoryFormData.fromData(category));
        model.addAttribute("editMode", EditMode.UPDATE);
        return "admin/category/form";
    }

    @PostMapping("/{id}/update")
    public String doUpdateCategory(
            @PathVariable("id") CategoryId id,
            @Valid @ModelAttribute("updateCategoryForm") UpdateCategoryFormData formData,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "admin/category/form";
        }

        try {
            categoryService.update(id, formData.toParameters());
            return "redirect:/admin/categories";
        } catch (BusinessException e) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/category/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String doDeleteCategory(@PathVariable("id") CategoryId id, RedirectAttributes redirectAttributes) {
        Category category = categoryService.getById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("deletedCategoryName", category.getCategoryName().asString());
        return "redirect:/admin/categories";
    }
}