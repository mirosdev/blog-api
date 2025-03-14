package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.util.Date;
import java.util.UUID;

@Entity
public class BlogArticleComment {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_comment_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_comment_content", length = 10485760)
    private String content;

    @Column(nullable = false, name = "blog_article_comment_date_created")
    private Date dateCreated;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
        this.dateCreated = new Date();
    }

    public String getContent() {
        return content;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
