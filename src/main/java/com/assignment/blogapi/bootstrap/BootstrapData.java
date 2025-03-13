package com.assignment.blogapi.bootstrap;

import com.assignment.blogapi.model.*;
import com.assignment.blogapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {
    private final BlogUserRepository blogUserRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final BlogArticleRepository blogArticleRepository;
    private final BlogArticleCommentRepository blogArticleCommentRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public BootstrapData(BlogUserRepository blogUserRepository,
                         RoleRepository roleRepository,
                         PrivilegeRepository privilegeRepository,
                         PasswordEncoder passwordEncoder,
                         BlogArticleRepository blogArticleRepository,
                         BlogArticleCommentRepository blogArticleCommentRepository) {
        this.blogUserRepository = blogUserRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.blogArticleRepository = blogArticleRepository;
        this.blogArticleCommentRepository = blogArticleCommentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!this.blogUserRepository.existsByEmail("bloguser@fake.com")) {
            this.createUser();
        }
        if (!this.blogUserRepository.existsByEmail("blogauthor@fake.com")) {
            this.createAuthorUser();
        }
        if (this.blogArticleRepository.count() == 0) {
            this.createBlogArticleAndComments();
            this.createBlogArticleAndComments();
            this.createBlogArticleAndComments();
            this.createBlogArticleAndComments();
        }
    }

    private void createUser() {
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail("bloguser@fake.com");
        blogUser.setPassword(this.passwordEncoder.encode("password"));
        blogUser.setFirstName("John");
        blogUser.setLastName("Smith");
        Set<Privilege> userPrivileges = Privilege.userPrivilegeSet();
        Set<Role> roles = Role.singleUserRole(userPrivileges);
        blogUser.setRoles(roles);

        try {
            privilegeRepository.saveAll(userPrivileges);
            roleRepository.saveAll(roles);
            blogUserRepository.save(blogUser);
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
        }
    }

    private void createAuthorUser() {
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail("blogauthor@fake.com");
        blogUser.setPassword(this.passwordEncoder.encode("password"));
        blogUser.setFirstName("John");
        blogUser.setLastName("Smith");
        Set<Privilege> authorPrivileges = Privilege.authorPrivilegeSet();
        Set<Role> roles = Role.singleAuthorRole(authorPrivileges);
        blogUser.setRoles(roles);

        try {
            privilegeRepository.saveAll(authorPrivileges);
            roleRepository.saveAll(roles);
            blogUserRepository.save(blogUser);
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
        }
    }

    private void createBlogArticleAndComments() {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTitle("This is a blog article title");
        blogArticle.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        BlogArticleComment blogArticleComment = new BlogArticleComment();
        blogArticleComment.setContent("This is a blog article comment");
        BlogArticleComment blogArticleComment2 = new BlogArticleComment();
        blogArticleComment2.setContent("This is second blog article comment");
        Set<BlogArticleComment> comments = new HashSet<>();
        comments.add(blogArticleComment);
        comments.add(blogArticleComment2);
        List<BlogArticleComment> returnedComments = this.blogArticleCommentRepository.saveAll(comments);
        blogArticle.setComments(returnedComments);
        this.blogArticleRepository.save(blogArticle);
    }
}
