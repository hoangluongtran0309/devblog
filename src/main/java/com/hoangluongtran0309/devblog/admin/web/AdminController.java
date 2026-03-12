package com.hoangluongtran0309.devblog.admin.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hoangluongtran0309.devblog.admin.PasswordNotMatchException;
import com.hoangluongtran0309.devblog.admin.UserService;
import com.hoangluongtran0309.devblog.infrastructure.security.ApplicationUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordFormData());
        return "admin/change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(
            @Valid @ModelAttribute("changePasswordForm") ChangePasswordFormData formData,
            BindingResult bindingResult,
            @AuthenticationPrincipal ApplicationUserDetails applicationUserDetails,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("changePasswordForm", formData);
            return "admin/change-password";
        }

        try {
            userService.changePassword(applicationUserDetails.getId(), formData.toParameters());
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, authentication);

            redirectAttributes.addFlashAttribute("alertType", "success");
            redirectAttributes.addFlashAttribute(
                    "alertMessage",
                    "Password changed successfully. Please login again.");

            return "redirect:/admin/login";

        } catch (PasswordNotMatchException ex) {
            model.addAttribute("changePasswordForm", formData);
            model.addAttribute("alertType", "error");
            model.addAttribute("alertMessage", ex.getMessage());
            return "admin/change-password";
        }
    }
}