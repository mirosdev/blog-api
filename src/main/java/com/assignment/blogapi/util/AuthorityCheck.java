package com.assignment.blogapi.util;

import com.assignment.blogapi.dto.BlogUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;


public class AuthorityCheck {
    public static void hasAuthority(Authentication auth, String authority) {
        if (((BlogUserDto) auth.getPrincipal()).getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public static BlogUserDto getCurrentUser(Authentication auth) {
        if (auth != null && auth.getPrincipal() != null) {
            return (BlogUserDto) auth.getPrincipal();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
