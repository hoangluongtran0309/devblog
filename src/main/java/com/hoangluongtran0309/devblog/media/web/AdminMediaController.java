package com.hoangluongtran0309.devblog.media.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.media.Media;
import com.hoangluongtran0309.devblog.media.MediaId;
import com.hoangluongtran0309.devblog.media.MediaNotFoundException;
import com.hoangluongtran0309.devblog.media.MediaService;

@Controller
@RequestMapping("/admin/media")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMediaController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMediaController.class);

    private final MediaService mediaService;

    public AdminMediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public String listMedia(
            @PageableDefault(size = 24, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String type,
            Model model) {
        Page<Media> media = switch (Optional.ofNullable(type).orElse("all").toLowerCase()) {
            case "image" -> mediaService.getImages(pageable);
            case "video" -> mediaService.getVideos(pageable);
            default -> mediaService.getAll(pageable);
        };

        model.addAttribute("page", media);
        model.addAttribute("selectedType", Optional.ofNullable(type).orElse("all"));
        return "admin/media/list";
    }

    @GetMapping("/picker")
    public String mediaPicker(
            @PageableDefault(size = 24, sort = "createdAt") Pageable pageable,
            Model model) {
        Page<Media> images = mediaService.getImages(pageable);
        model.addAttribute("pickerPage", images);
        return "admin/media/picker";
    }

    @PostMapping("/upload")
    public String doUploadMedia(
            @RequestParam("files") List<MultipartFile> files,
            RedirectAttributes redirectAttributes) {
        try {
            int count = 0;
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    mediaService.upload(file);
                    count++;
                }
            }
            redirectAttributes.addFlashAttribute("successMessage",
                    count + " file(s) uploaded successfully.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during media upload", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred. Please try again.");
        }
        return "redirect:/admin/media";
    }

    @PostMapping("/{id}/delete")
    public String doDeleteMedia(
            @PathVariable("id") MediaId id,
            RedirectAttributes redirectAttributes) {
        Media media = mediaService.getById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        try {
            mediaService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "\"" + media.getMediaFileName().asString() + "\" deleted successfully.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during media deletion, ID: {}", id.asString(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred. Please try again.");
        }
        return "redirect:/admin/media";
    }
}