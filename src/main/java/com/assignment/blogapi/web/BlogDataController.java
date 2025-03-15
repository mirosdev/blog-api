package com.assignment.blogapi.web;


import com.assignment.blogapi.dto.BlogArticleCommentRequest;
import com.assignment.blogapi.dto.BlogArticleLikeRequest;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.model.BlogArticleComment;
import com.assignment.blogapi.model.BlogArticleLike;
import com.assignment.blogapi.service.BlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        if (blogArticleCommentRequest.getArticleUuid() == null || blogArticleCommentRequest.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(blogArticleService.commentOnArticle(blogArticleCommentRequest));
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteArticleComment(@RequestBody BlogArticleCommentRequest blogArticleCommentRequest) {
        if (blogArticleCommentRequest.getCommentUuid() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.blogArticleService.deleteArticleComment(blogArticleCommentRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like")
    public ResponseEntity<BlogArticleLike> toggleLikeOnArticle(@RequestBody BlogArticleLikeRequest blogArticleLikeRequest, Authentication authentication) {
        if (blogArticleLikeRequest.getArticleUuid() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(blogArticleService.toggleLikeOnArticle(blogArticleLikeRequest, authentication.getName()));
    }

}
