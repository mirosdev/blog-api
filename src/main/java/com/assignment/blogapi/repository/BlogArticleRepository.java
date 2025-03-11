package com.assignment.blogapi.repository;


import com.assignment.blogapi.model.BlogArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogArticleRepository extends JpaRepository<BlogArticle, UUID> {
    Optional<BlogArticle> findByUuid(UUID uuid);
}
