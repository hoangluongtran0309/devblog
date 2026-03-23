package com.hoangluongtran0309.devblog.content.tag.web;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagName;
import com.hoangluongtran0309.devblog.content.tag.TagParameters;

public class CreateTagFormData extends AbstractTagFormData {

    public TagParameters toParameters() {
        return new TagParameters(
                new TagName(getTagName()),
                new Slug(getTagSlug()));
    }
}
