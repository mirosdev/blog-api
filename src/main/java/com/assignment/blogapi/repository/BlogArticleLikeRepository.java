package com.assignment.blogapi.repository;

import com.assignment.blogapi.model.BlogArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogArticleLikeRepository extends JpaRepository<BlogArticleLike, UUID> {
}
