package com.hoangluongtran0309.devblog.media;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MediaFileName {

    private String mediaFileName;

    protected MediaFileName() {

    }

    public MediaFileName(String mediaFileName) {
        Assert.hasText(mediaFileName, "mediaFileName cannot be blank");
        this.mediaFileName = mediaFileName;
    }

    public String asString() {
        return mediaFileName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mediaFileName == null) ? 0 : mediaFileName.hashCode());
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
        MediaFileName other = (MediaFileName) obj;
        if (mediaFileName == null) {
            if (other.mediaFileName != null)
                return false;
        } else if (!mediaFileName.equals(other.mediaFileName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mediaFileName", mediaFileName)
                .toString();
    }
}
