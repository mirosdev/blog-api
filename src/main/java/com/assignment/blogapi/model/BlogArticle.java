package com.assignment.blogapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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



}
