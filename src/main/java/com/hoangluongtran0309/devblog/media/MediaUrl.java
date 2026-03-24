package com.hoangluongtran0309.devblog.media;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MediaUrl {

    private String mediaUrl;

    protected MediaUrl() {

    }

    public MediaUrl(String mediaUrl) {
        Assert.hasText(mediaUrl, "mediaUrl cannot be blank");
        Assert.isTrue(mediaUrl.startsWith("https://"), "mediaUrl should starts with https://");
        this.mediaUrl = mediaUrl;
    }

    public String asString() {
        return mediaUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mediaUrl == null) ? 0 : mediaUrl.hashCode());
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
        MediaUrl other = (MediaUrl) obj;
        if (mediaUrl == null) {
            if (other.mediaUrl != null)
                return false;
        } else if (!mediaUrl.equals(other.mediaUrl))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mediaUrl", mediaUrl)
                .toString();
    }
}
