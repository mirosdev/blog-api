package com.assignment.blogapi.bootstrap;

import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.model.Privilege;
import com.assignment.blogapi.model.Role;
import com.assignment.blogapi.repository.PrivilegeRepository;
import com.assignment.blogapi.repository.RoleRepository;
import com.assignment.blogapi.repository.BlogUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {
    private final BlogUserRepository blogUserRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BootstrapData(BlogUserRepository blogUserRepository,
                         RoleRepository roleRepository,
                         PrivilegeRepository privilegeRepository,
                         PasswordEncoder passwordEncoder) {
        this.blogUserRepository = blogUserRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
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
    }

    private void createUser() {
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail("bloguser@fake.com");
        blogUser.setPassword(this.passwordEncoder.encode("password"));
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
}
