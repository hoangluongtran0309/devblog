package com.hoangluongtran0309.devblog.content.tag;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public class TagParameters {

    private final TagName tagName;
    private final Slug tagSlug;

    public TagParameters(TagName tagName, Slug tagSlug) {
        this.tagName = tagName;
        this.tagSlug = tagSlug;
    }

    public TagName getTagName() {
        return tagName;
    }

    public Slug getTagSlug() {
        return tagSlug;
    }
}
