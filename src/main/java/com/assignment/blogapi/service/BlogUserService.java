package com.assignment.blogapi.service;

import com.assignment.blogapi.dto.BlogUserDto;
import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.model.Privilege;
import com.assignment.blogapi.model.Role;
import com.assignment.blogapi.repository.PrivilegeRepository;
import com.assignment.blogapi.repository.RoleRepository;
import com.assignment.blogapi.repository.BlogUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Set;

@Service
public class BlogUserService {
    private final BlogUserRepository blogUserRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public BlogUserService(BlogUserRepository blogUserRepository,
                           RoleRepository roleRepository,
                           PrivilegeRepository privilegeRepository,
                           PasswordEncoder passwordEncoder) {
        this.blogUserRepository = blogUserRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public BlogUserDto register(String username, String password) {
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail(username);
        blogUser.setPassword(this.passwordEncoder.encode(password));
        blogUser.setRoles(getUserRoles());

        try {
            BlogUser saved = this.blogUserRepository.save(blogUser);
            return new BlogUserDto(saved.getUuid(), saved.getEmail());
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + username + " already exists");
        }
    }

    private Set<Role> getUserRoles() {
        Set<Privilege> userPrivileges = Privilege.userPrivilegeSet();
        Set<Role> userRoles = Role.singleUserRole(userPrivileges);

        try {
            privilegeRepository.saveAll(userPrivileges);
            roleRepository.saveAll(userRoles);
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return userRoles;
    }

}
