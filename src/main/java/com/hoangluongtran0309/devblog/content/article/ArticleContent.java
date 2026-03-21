package com.hoangluongtran0309.devblog.content.article;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ArticleContent {

    private String articleContent;

    protected ArticleContent() {

    }

    public ArticleContent(String articleContent) {
        Assert.hasText(articleContent, "articleContent cannot be blank");
        this.articleContent = articleContent;
    }

    public String asString() {
        return articleContent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((articleContent == null) ? 0 : articleContent.hashCode());
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
        ArticleContent other = (ArticleContent) obj;
        if (articleContent == null) {
            if (other.articleContent != null)
                return false;
        } else if (!articleContent.equals(other.articleContent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("articleContent", articleContent)
                .toString();
    }
}
