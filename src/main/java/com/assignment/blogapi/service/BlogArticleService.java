package com.assignment.blogapi.service;


import com.assignment.blogapi.dto.BlogArticleCommentRequest;
import com.assignment.blogapi.dto.BlogArticleLikeRequest;
import com.assignment.blogapi.model.BlogArticle;
import com.assignment.blogapi.model.BlogArticleComment;
import com.assignment.blogapi.model.BlogArticleLike;
import com.assignment.blogapi.repository.BlogArticleCommentRepository;
import com.assignment.blogapi.repository.BlogArticleLikeRepository;
import com.assignment.blogapi.repository.BlogArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogArticleService {

    private final BlogArticleRepository blogArticleRepository;
    private final BlogArticleCommentRepository blogArticleCommentRepository;
    private final BlogArticleLikeRepository blogArticleLikeRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public BlogArticleService(BlogArticleRepository blogArticleRepository,
                              BlogArticleCommentRepository blogArticleCommentRepository,
                              BlogArticleLikeRepository blogArticleLikeRepository) {
        this.blogArticleRepository = blogArticleRepository;
        this.blogArticleCommentRepository = blogArticleCommentRepository;
        this.blogArticleLikeRepository = blogArticleLikeRepository;
    }

    public Collection<BlogArticle> getBlogArticles() {
        Collection<BlogArticle> blogArticles;
        try {
            blogArticles = this.blogArticleRepository.findAll().stream().sorted(
                    (o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated())
            ).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        blogArticles.forEach(blogArticle -> {
            blogArticle.setComments(blogArticle.getComments().stream().sorted(
                    (o1, o2) -> o1.getDateCreated().compareTo(o2.getDateCreated())
            ).collect(Collectors.toList()));
        });
        return blogArticles;
    }

    public BlogArticleLike toggleLikeOnArticle(BlogArticleLikeRequest blogArticleLikeRequest, String userUuid) {
        Optional<BlogArticle> optionalBlogArticle;
        try {
            optionalBlogArticle = this.blogArticleRepository.findByUuid(UUID.fromString(blogArticleLikeRequest.getArticleUuid()));
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (optionalBlogArticle.isPresent()) {
            BlogArticleLike selectedBlogArticleLike;
            BlogArticle blogArticle = optionalBlogArticle.get();

            // Removing like
            if (blogArticle.getLikes().stream().anyMatch(like -> like.getBlogUserUuid().equals(UUID.fromString(userUuid)))) {
                selectedBlogArticleLike = blogArticle.getLikes().stream()
                        .filter(like -> like.getBlogUserUuid().equals(UUID.fromString(userUuid))).findFirst().orElse(null);
                blogArticle.setLikes(blogArticle.getLikes().stream()
                        .filter(like -> !like.getBlogUserUuid().equals(UUID.fromString(userUuid)))
                        .collect(Collectors.toList()));

                if (selectedBlogArticleLike != null) {
                    try {
                        this.blogArticleRepository.save(blogArticle);
                        this.blogArticleLikeRepository.deleteById(selectedBlogArticleLike.getUuid());
                    } catch (Exception e) {
                        logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return selectedBlogArticleLike;
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }

                // Adding like
            } else {
                // Like prep
                BlogArticleLike blogArticleLike = new BlogArticleLike();
                blogArticleLike.setBlogUserUuid(UUID.fromString(userUuid));

                // Article prep
                Set<BlogArticleLike> likes = new HashSet<>(blogArticle.getLikes());
                likes.add(blogArticleLike);

                blogArticle.setLikes(likes);

                BlogArticleLike newBlogArticleLike;
                try {
                    newBlogArticleLike = this.blogArticleLikeRepository.save(blogArticleLike);
                    this.blogArticleRepository.save(blogArticle);
                } catch (Exception e) {
                    logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }

                return newBlogArticleLike;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public BlogArticleComment commentOnArticle(BlogArticleCommentRequest blogArticleCommentRequest) {
        Optional<BlogArticle> optionalBlogArticle;
        try {
            optionalBlogArticle = this.blogArticleRepository.findByUuid(UUID.fromString(blogArticleCommentRequest.getArticleUuid()));
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (optionalBlogArticle.isPresent()) {
            // Comment prep
            BlogArticleComment blogArticleComment = new BlogArticleComment();
            blogArticleComment.setContent(blogArticleCommentRequest.getContent());

            // Article prep
            BlogArticle blogArticle = optionalBlogArticle.get();
            Set<BlogArticleComment> comments = new HashSet<>(blogArticle.getComments());
            comments.add(blogArticleComment);

            blogArticle.setComments(comments);

            BlogArticleComment newBlogArticleComment;
            try {
                newBlogArticleComment = this.blogArticleCommentRepository.save(blogArticleComment);
                this.blogArticleRepository.save(blogArticle);
            } catch (Exception e) {
                logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return newBlogArticleComment;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteArticleComment(BlogArticleCommentRequest blogArticleCommentRequest) {
        Optional<BlogArticle> optionalBlogArticle;
        try {
            optionalBlogArticle = this.blogArticleRepository.findByUuid(UUID.fromString(blogArticleCommentRequest.getArticleUuid()));
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (optionalBlogArticle.isPresent()) {
            BlogArticle blogArticle = optionalBlogArticle.get();
            blogArticle.setComments(blogArticle.getComments()
                    .stream().filter((blogArticleComment) -> !blogArticleComment.getUuid().equals(UUID.fromString(blogArticleCommentRequest.getCommentUuid())))
                    .collect(Collectors.toList()));
            try {
                this.blogArticleCommentRepository.deleteById(UUID.fromString(blogArticleCommentRequest.getCommentUuid()));
                this.blogArticleRepository.save(blogArticle);
            } catch (Exception e) {
                logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
