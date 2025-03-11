package com.assignment.blogapi.web;


import com.assignment.blogapi.dto.BlogArticleRequestPayload;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.service.BlogAuthoringService;
import com.assignment.blogapi.util.AuthorityCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.assignment.blogapi.security.BlogAuthorities.Privileges.AUTHOR_PRIVILEGE;

@RestController
@RequestMapping("/api/author")
public class BlogAuthoringController {
    private final BlogAuthoringService blogAuthoringService;

    @Autowired
    public BlogAuthoringController(BlogAuthoringService blogAuthoringService) {
        this.blogAuthoringService = blogAuthoringService;
    }

    @PostMapping("/article")
    public ResponseEntity<BlogArticle> createArticle(@RequestBody BlogArticleRequestPayload payload, Authentication auth) {
        AuthorityCheck.hasAuthority(auth, AUTHOR_PRIVILEGE);
        return ResponseEntity.ok(this.blogAuthoringService.createBlogArticle(payload));
    }

    @PutMapping("/article")
    public ResponseEntity<BlogArticle> updateArticle(@RequestBody BlogArticleRequestPayload payload, Authentication auth) {
        AuthorityCheck.hasAuthority(auth, AUTHOR_PRIVILEGE);
        return ResponseEntity.ok(this.blogAuthoringService.updateBlogArticle(payload));
    }

    @DeleteMapping("/article")
    public ResponseEntity<?> deleteArticle(@RequestBody BlogArticleRequestPayload payload, Authentication auth) {
        AuthorityCheck.hasAuthority(auth, AUTHOR_PRIVILEGE);
        this.blogAuthoringService.deleteBlogArticle(payload);
        return ResponseEntity.ok().build();
    }
}
