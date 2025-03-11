package com.assignment.blogapi.service;


import com.assignment.blogapi.dto.BlogArticleRequestPayload;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.repository.BlogArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class BlogAuthoringService {
    private final BlogArticleRepository blogArticleRepository;

    @Autowired
    public BlogAuthoringService(BlogArticleRepository blogArticleRepository) {
        this.blogArticleRepository = blogArticleRepository;
    }

    public BlogArticle createBlogArticle(BlogArticleRequestPayload blogArticleRequestPayload) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTitle(blogArticleRequestPayload.getTitle());
        blogArticle.setContent(blogArticleRequestPayload.getContent());
        try {
            return this.blogArticleRepository.save(blogArticle);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BlogArticle updateBlogArticle(BlogArticleRequestPayload blogArticleRequestPayload) {
        Optional<BlogArticle> optionalBlogArticle;
        try {
            optionalBlogArticle = this.blogArticleRepository.findByUuid(UUID.fromString(blogArticleRequestPayload.getUuid()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (optionalBlogArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        BlogArticle blogArticle = optionalBlogArticle.get();
        blogArticle.setTitle(blogArticleRequestPayload.getTitle());
        blogArticle.setContent(blogArticleRequestPayload.getContent());

        try {
            return this.blogArticleRepository.save(blogArticle);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteBlogArticle(BlogArticleRequestPayload blogArticleRequestPayload) {
        Optional<BlogArticle> optionalBlogArticle;
        try {
            optionalBlogArticle = this.blogArticleRepository.findByUuid(UUID.fromString(blogArticleRequestPayload.getUuid()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (optionalBlogArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            this.blogArticleRepository.deleteById(optionalBlogArticle.get().getUuid());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
