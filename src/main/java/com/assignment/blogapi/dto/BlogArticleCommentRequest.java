package com.assignment.blogapi.dto;

import java.util.UUID;

public class BlogArticleCommentRequest {
    private UUID articleUuid;
    private String content;

    public String getContent() {
        return content;
    }

    public UUID getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(UUID articleUuid) {
        this.articleUuid = articleUuid;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
