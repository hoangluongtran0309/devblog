package com.hoangluongtran0309.devblog.content.article;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ArticleTitle {

    private String articleTitle;

    protected ArticleTitle() {

    }

    public ArticleTitle(String articleTitle) {
        Assert.hasText(articleTitle, "articleTitle cannot be blank");
        this.articleTitle = articleTitle;
    }

    public String asString() {
        return articleTitle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((articleTitle == null) ? 0 : articleTitle.hashCode());
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
        ArticleTitle other = (ArticleTitle) obj;
        if (articleTitle == null) {
            if (other.articleTitle != null)
                return false;
        } else if (!articleTitle.equals(other.articleTitle))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("articleTitle", articleTitle)
                .toString();
    }
}
