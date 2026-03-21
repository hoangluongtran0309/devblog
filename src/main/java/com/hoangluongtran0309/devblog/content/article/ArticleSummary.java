package com.hoangluongtran0309.devblog.content.article;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ArticleSummary {

    private String articleSummary;

    protected ArticleSummary() {

    }

    public ArticleSummary(String articleSummary) {
        Assert.hasText(articleSummary, "articleSummary cannot be blank");
        this.articleSummary = articleSummary;
    }

    public String asString() {
        return articleSummary;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((articleSummary == null) ? 0 : articleSummary.hashCode());
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
        ArticleSummary other = (ArticleSummary) obj;
        if (articleSummary == null) {
            if (other.articleSummary != null)
                return false;
        } else if (!articleSummary.equals(other.articleSummary))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("articleSummary", articleSummary)
                .toString();
    }
}
