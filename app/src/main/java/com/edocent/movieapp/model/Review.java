package com.edocent.movieapp.model;

/**
 * Created by SRIVASTAVAA on 10/26/2015.
 */
public class Review {

    long reviewId;
    String author;
    String content;
    String url;

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
