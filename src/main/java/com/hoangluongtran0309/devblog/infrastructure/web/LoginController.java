package com.hoangluongtran0309.devblog.infrastructure.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/login")
public class LoginController {

    @GetMapping
    public String index(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("alertType", "error");
            model.addAttribute("alertMessage", "Invalid username or password.");
        }

        if (logout != null) {
            model.addAttribute("alertType", "success");
            model.addAttribute("alertMessage", "You have been logged out successfully.");
        }

        return "admin/login";
    }
}
