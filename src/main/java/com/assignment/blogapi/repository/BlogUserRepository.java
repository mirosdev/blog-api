package com.assignment.blogapi.repository;

import com.assignment.blogapi.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, UUID> {
    Optional<BlogUser> findByEmail(String email);
}
