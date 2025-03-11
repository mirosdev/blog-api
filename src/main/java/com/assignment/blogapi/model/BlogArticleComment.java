package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.util.UUID;

@Entity
public class BlogArticleComment {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_comment_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_comment_content")
    private String content;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    public String getContent() {
        return content;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
