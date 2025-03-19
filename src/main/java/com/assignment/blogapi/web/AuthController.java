package com.assignment.blogapi.web;

import com.assignment.blogapi.dto.*;
import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.security.ExceptionHandlerFilter;
import com.assignment.blogapi.service.BlogUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final BlogUserService blogUserService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          BlogUserService blogUserService) {
        this.authenticationManager = authenticationManager;
        this.blogUserService = blogUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<BlogUserDto> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        if (authenticationRequest.getUsername() == null || authenticationRequest.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // blogUserService.getBlogUserByUsername() called once for further storing UUID in JWT and Context instead of user's username
        // so it can be checked against database on verify
        BlogUser blogUser = blogUserService.getBlogUserByUsername(authenticationRequest.getUsername());
        Cookie cookie = this.blogUserService.setupAuthCookie(blogUser);
        response.addCookie(cookie);
        response.addHeader(cookie.getName(), cookie.getValue());

        return ResponseEntity.ok(BlogUserService.mapUserData(blogUser));
    }

    @PostMapping("/register")
    public ResponseEntity<BlogUserDto> register(@RequestBody AuthRegistrationRequest authRegistrationRequest, HttpServletResponse response) {
        if (authRegistrationRequest.getUsername() == null || authRegistrationRequest.getPassword() == null
                || authRegistrationRequest.getFirstName() == null || authRegistrationRequest.getLastName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        BlogUser blogUser;
        try {
            blogUser = blogUserService.register(authRegistrationRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(blogUser.getEmail(), authRegistrationRequest.getPassword())
        );
        response.addCookie(this.blogUserService.setupAuthCookie(blogUser));

        return ResponseEntity.ok(BlogUserService.mapUserData(blogUser));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<BlogUserDto> isAuthenticated(Authentication auth) {
        BlogUserDto blogUserDto = null;
        if (auth.getPrincipal() != null) {
            blogUserDto = (BlogUserDto) auth.getPrincipal();
        }

        return ResponseEntity.ok(blogUserDto);
    }

    @PostMapping("/username-check")
    public ResponseEntity<UsernameCheckResponse> checkUsernameAvailability(@RequestBody UsernameCheckRequest usernameCheckRequest) {
        if (usernameCheckRequest.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new UsernameCheckResponse(this.blogUserService.checkUsernameAvailability(usernameCheckRequest)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.addCookie(ExceptionHandlerFilter.getSessionRemoverCookie());

        return ResponseEntity.ok().build();
    }
}
