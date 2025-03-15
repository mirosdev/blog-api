package com.assignment.blogapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
public class BlogArticle {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_title")
    private String title;

    @Column(nullable = false, name = "blog_article_content", length = 10485760)
    private String content;

    @Column(nullable = false, name = "blog_article_date_created")
    private Date dateCreated;

    @OneToMany(mappedBy = "blogArticle")
    @JsonManagedReference
    private Collection<BlogArticleComment> comments;

    @OneToMany(mappedBy = "blogArticle")
    @JsonManagedReference
    private Collection<BlogArticleLike> likes;

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

    public Collection<BlogArticleComment> getComments() {
        return comments;
    }

    public Collection<BlogArticleLike> getLikes() {
        return likes;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setComments(Collection<BlogArticleComment> comments) {
        this.comments = comments;
    }

    public void setLikes(Collection<BlogArticleLike> likes) {
        this.likes = likes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}
