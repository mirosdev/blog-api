package com.assignment.blogapi.dto;

import java.util.UUID;

public class BlogArticleLikeRequest {
    private UUID articleUuid;

    public UUID getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(UUID articleUuid) {
        this.articleUuid = articleUuid;
    }
}
