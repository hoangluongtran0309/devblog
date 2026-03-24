package com.hoangluongtran0309.devblog.media;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MediaPublicId {

    private String mediaPublicId;

    protected MediaPublicId() {

    }

    public MediaPublicId(String mediaPublicId) {
        Assert.hasText(mediaPublicId, "mediaPublicId cannot be blank");
        this.mediaPublicId = mediaPublicId;
    }

    public String asString() {
        return mediaPublicId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mediaPublicId == null) ? 0 : mediaPublicId.hashCode());
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
        MediaPublicId other = (MediaPublicId) obj;
        if (mediaPublicId == null) {
            if (other.mediaPublicId != null)
                return false;
        } else if (!mediaPublicId.equals(other.mediaPublicId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mediaPublicId", mediaPublicId)
                .toString();
    }
}
