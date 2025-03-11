package com.assignment.blogapi.web;

import com.assignment.blogapi.dto.BlogUserDto;
import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.security.AuthenticationRequest;
import com.assignment.blogapi.security.JwtUtil;
import com.assignment.blogapi.dto.LoginSuccessResponse;
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // blogUserService.getBlogUserByUsername() called once for further storing UUID in JWT and Context instead of user's username
        BlogUser blogUser = blogUserService.getBlogUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(blogUser);

        return ResponseEntity.ok(new LoginSuccessResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<BlogUserDto> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(blogUserService.register(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    }
}
