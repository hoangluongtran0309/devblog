package com.hoangluongtran0309.devblog.content.tag;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "db_tag")
public class Tag extends BaseEntity<TagId> {

    @Embedded
    @NotNull
    private TagName tagName;

    @Embedded
    @AttributeOverride(name = "slug", column = @Column(name = "tag_slug"))
    @NotNull
    private Slug tagSlug;

    protected Tag() {

    }

    public Tag(TagId id, TagName tagName, Slug tagSlug) {
        super(id);
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
