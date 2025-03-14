package com.assignment.blogapi.web;

import com.assignment.blogapi.dto.*;
import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.security.JwtUtil;
import com.assignment.blogapi.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final BlogUserService blogUserService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          BlogUserService blogUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.blogUserService = blogUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getUsername() == null || authenticationRequest.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // blogUserService.getBlogUserByUsername() called once for further storing UUID in JWT and Context instead of user's username
        BlogUser blogUser = blogUserService.getBlogUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(blogUser);

        return ResponseEntity.ok(new LoginSuccessResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginSuccessResponse> register(@RequestBody AuthRegistrationRequest authRegistrationRequest) {
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
        final String jwt = jwtUtil.generateToken(blogUser);

        return ResponseEntity.ok(new LoginSuccessResponse(jwt));
    }

    @PostMapping("/username-check")
    public ResponseEntity<UsernameCheckResponse> checkUsernameAvailability(@RequestBody UsernameCheckRequest usernameCheckRequest) {
        if (usernameCheckRequest.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new UsernameCheckResponse(this.blogUserService.checkUsernameAvailability(usernameCheckRequest)));
    }
}
