package com.assignment.blogapi.security;

import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.repository.BlogUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        Optional<BlogUser> blogUser = blogUserRepository.findByEmail(username);
        if (blogUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(blogUser.get().getUuid().toString(), blogUser.get().getPassword(), blogUser.get().getAuthorities());
    }

}
