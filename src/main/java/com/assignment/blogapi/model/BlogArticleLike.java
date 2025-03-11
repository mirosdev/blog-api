package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.util.UUID;

@Entity
public class BlogArticleLike {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_like_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_like_user_id")
    private UUID blogUserUuid;

    @PrePersist
    public void generateOnCreate() {
        this.uuid = Generators.timeBasedGenerator().generate();
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getBlogUserUuid() {
        return blogUserUuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setBlogUserUuid(UUID blogUserUuid) {
        this.blogUserUuid = blogUserUuid;
    }
}
