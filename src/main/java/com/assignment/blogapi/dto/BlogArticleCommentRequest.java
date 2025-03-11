package com.assignment.blogapi.dto;

public class BlogArticleCommentRequest {
    private String articleUuid;
    private String commentUuid;
    private String content;

    public String getContent() {
        return content;
    }

    public String getArticleUuid() {
        return articleUuid;
    }

    public String getCommentUuid() {
        return commentUuid;
    }

    public void setArticleUuid(String articleUuid) {
        this.articleUuid = articleUuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCommentUuid(String commentUuid) {
        this.commentUuid = commentUuid;
    }
}
