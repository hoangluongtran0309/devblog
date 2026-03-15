package com.hoangluongtran0309.devblog.content.tag;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoangluongtran0309.devblog.content.shared.Slug;

class TagTest {

    private TagId tagId;
    private TagName tagName;
    private Slug slug;

    @BeforeEach
    void setUp() {
        tagId = new TagId();
        tagName = new TagName("Spring Boot");
        slug = new Slug("spring-boot");
    }

    @Test
    void shouldCreateTag() {

        Tag tag = new Tag(
                tagId,
                tagName,
                slug);

        assertThat(tag.getTagName()).isEqualTo(tagName);
        assertThat(tag.getTagSlug()).isEqualTo(slug);
    }

    @Test
    void tagShouldHaveCorrectValues() {

        Tag tag = new Tag(
                tagId,
                tagName,
                slug);

        assertThat(tag.getTagName().toString()).contains("Spring");
        assertThat(tag.getTagSlug().toString()).contains("spring");
    }
}