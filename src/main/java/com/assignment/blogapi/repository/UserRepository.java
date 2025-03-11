package com.assignment.blogapi.repository;

import com.assignment.blogapi.model.Role;
import com.assignment.blogapi.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<BlogUser, UUID> {
    Optional<BlogUser> findByUuid(UUID uuid);

    Optional<BlogUser> findByEmail(String email);

    @Query("SELECT u.roles FROM BlogUser u WHERE u.uuid = ?1")
    Collection<Role> customFindUserRolesByUserUuid(UUID uuid);

    @Query("SELECT u.uuid FROM BlogUser u WHERE u.email = ?1")
    Optional<UUID> customFindUserUuidByUserEmail(String email);

    Boolean existsByEmail(String email);
}
