package com.assignment.blogapi.web;


import com.assignment.blogapi.dto.BlogArticleCommentRequest;
import com.assignment.blogapi.dto.BlogArticleLikeRequest;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.model.BlogArticleComment;
import com.assignment.blogapi.model.BlogArticleLike;
import com.assignment.blogapi.service.BlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/data")
public class BlogDataController {
    private final BlogArticleService blogArticleService;

    @Autowired
    public BlogDataController(BlogArticleService blogArticleService) {
        this.blogArticleService = blogArticleService;
    }

    @GetMapping("/article")
    public ResponseEntity<Collection<BlogArticle>> getBlogArticles() {
        return ResponseEntity.ok(blogArticleService.getBlogArticles());
    }

    @PostMapping("/comment")
    public ResponseEntity<BlogArticleComment> commentOnArticle(@RequestBody BlogArticleCommentRequest blogArticleCommentRequest) {
        return ResponseEntity.ok(blogArticleService.commentOnArticle(blogArticleCommentRequest));
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteArticleComment(@RequestBody BlogArticleCommentRequest blogArticleCommentRequest) {
        this.blogArticleService.deleteArticleComment(blogArticleCommentRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like")
    public ResponseEntity<BlogArticleLike> likeOnArticle(@RequestBody BlogArticleLikeRequest blogArticleLikeRequest, Authentication authentication) {
        return ResponseEntity.ok(blogArticleService.likeOnArticle(blogArticleLikeRequest, authentication.getName()));
    }

}
