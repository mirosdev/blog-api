package com.assignment.blogapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;


public class AuthorityCheck {
    public static void hasAuthority(Authentication auth, String authority) {
        if (auth.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
