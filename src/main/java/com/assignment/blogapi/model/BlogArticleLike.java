package com.assignment.blogapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class BlogArticleLike {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_like_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_like_user_id")
    private UUID blogUserUuid;

    @ManyToOne
    @JoinColumn(name = "blog_article_id")
    @JsonBackReference
    private BlogArticle blogArticle;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    public BlogArticle getBlogArticle() {
        return blogArticle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getBlogUserUuid() {
        return blogUserUuid;
    }

    public void setBlogArticle(BlogArticle blogArticle) {
        this.blogArticle = blogArticle;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setBlogUserUuid(UUID blogUserUuid) {
        this.blogUserUuid = blogUserUuid;
    }
}
