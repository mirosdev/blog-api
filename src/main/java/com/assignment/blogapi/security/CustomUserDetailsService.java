package com.assignment.blogapi.security;

import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.repository.BlogUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BlogUserRepository blogUserRepository;

    @Autowired
    public CustomUserDetailsService(BlogUserRepository blogUserRepository) {
        this.blogUserRepository = blogUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BlogUser> user = blogUserRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new User(user.get().getEmail(), user.get().getPassword(), user.get().getAuthorities());
    }

}
