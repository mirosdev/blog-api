package com.assignment.blogapi.web;


import com.assignment.blogapi.dto.BlogArticleRequestPayload;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.service.BlogAuthoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/author")
public class BlogAuthoringController {
    private final BlogAuthoringService blogAuthoringService;

    @Autowired
    public BlogAuthoringController(BlogAuthoringService blogAuthoringService) {
        this.blogAuthoringService = blogAuthoringService;
    }

    @PostMapping("/article")
    public ResponseEntity<BlogArticle> createArticle(@RequestBody BlogArticleRequestPayload payload) {
        return ResponseEntity.ok(this.blogAuthoringService.createBlogArticle(payload));
    }

    @PutMapping("/article")
    public ResponseEntity<BlogArticle> updateArticle(@RequestBody BlogArticleRequestPayload payload) {
        return ResponseEntity.ok(this.blogAuthoringService.updateBlogArticle(payload));
    }

}
