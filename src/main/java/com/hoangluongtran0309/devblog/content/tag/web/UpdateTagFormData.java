package com.hoangluongtran0309.devblog.content.tag.web;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.content.tag.TagParameters;

public class UpdateTagFormData extends AbstractTagFormData {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TagParameters toParameters() {
        return new TagParameters(
                new TagName(getTagName()),
                new Slug(getTagSlug()));
    }

    public static UpdateTagFormData fromData(Tag tag) {
        UpdateTagFormData result = new UpdateTagFormData();

        result.setId(tag.getId().asString().toString());
        result.setTagName(tag.getTagName().asString());
        result.setTagSlug(tag.getTagSlug().asString());

        return result;
    }
}
