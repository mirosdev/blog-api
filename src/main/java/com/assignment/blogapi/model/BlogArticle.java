package com.assignment.blogapi.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;

@Entity
public class BlogArticle {
    @Id
    @Column(nullable = false, unique = true, name = "blog_article_id")
    private UUID uuid;

    @Column(nullable = false, name = "blog_article_title")
    private String title;

    @Column(nullable = false, name = "blog_article_content")
    private String content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = { @JoinColumn(name = "blog_article_id", referencedColumnName = "blog_article_id") }, inverseJoinColumns = { @JoinColumn(name = "blog_article_comment_id", referencedColumnName = "blog_article_comment_id") })
    private Collection<BlogArticleComment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = { @JoinColumn(name = "blog_article_id", referencedColumnName = "blog_article_id") }, inverseJoinColumns = { @JoinColumn(name = "blog_article_like_id", referencedColumnName = "blog_article_like_id") })
    private Collection<BlogArticleLike> likes;

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
}
