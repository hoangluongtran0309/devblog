package com.hoangluongtran0309.devblog.content.tag.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AbstractTagFormData {

    @NotBlank(message = "Tag name must not be blank")
    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
    private String tagName;

    @NotBlank(message = "Tag slug must not be blank")
    @Size(min = 2, max = 50, message = "Tag slug must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String tagSlug;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagSlug() {
        return tagSlug;
    }

    public void setTagSlug(String tagSlug) {
        this.tagSlug = tagSlug;
    }
}