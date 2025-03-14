package com.assignment.blogapi.service;

import com.assignment.blogapi.dto.AuthRegistrationRequest;
import com.assignment.blogapi.dto.UsernameCheckRequest;
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
import java.util.Optional;
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

    public Boolean checkUsernameAvailability(UsernameCheckRequest usernameCheckRequest) {
        try {
            return !this.blogUserRepository.existsByEmail(usernameCheckRequest.getUsername());
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BlogUser register(AuthRegistrationRequest authRegistrationRequest) {
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail(authRegistrationRequest.getUsername());
        blogUser.setPassword(this.passwordEncoder.encode(authRegistrationRequest.getPassword()));
        blogUser.setRoles(getUserRoles());
        blogUser.setFirstName(authRegistrationRequest.getFirstName());
        blogUser.setLastName(authRegistrationRequest.getLastName());

        try {
            return this.blogUserRepository.save(blogUser);
        } catch (Exception e) {
            logger.error(e.toString().concat(Arrays.asList(e.getStackTrace()).toString()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + authRegistrationRequest.getUsername() + " already exists");
        }
    }

    public BlogUser getBlogUserByUsername(String username) {
        Optional<BlogUser> blogUser;
        try {
            blogUser = this.blogUserRepository.findByEmail(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (blogUser.isPresent()) {
            return blogUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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
