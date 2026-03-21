package com.hoangluongtran0309.devblog.content.shared;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ImagePreview {

    private String imagePreview;

    protected ImagePreview() {

    }

    public ImagePreview(String imagePreview) {
        Assert.hasText(imagePreview, "imagePreview cannot be blank");
        Assert.isTrue(imagePreview.startsWith("https://"), "imagePreview should starts with https://");
        this.imagePreview = imagePreview;
    }

    public String asString() {
        return imagePreview;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imagePreview == null) ? 0 : imagePreview.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImagePreview other = (ImagePreview) obj;
        if (imagePreview == null) {
            if (other.imagePreview != null)
                return false;
        } else if (!imagePreview.equals(other.imagePreview))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("imagePreview", imagePreview)
                .toString();
    }
}
