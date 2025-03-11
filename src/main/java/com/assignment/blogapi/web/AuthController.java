package com.assignment.blogapi.web;

import com.assignment.blogapi.dto.BlogUserDto;
import com.assignment.blogapi.security.AuthenticationRequest;
import com.assignment.blogapi.security.CustomUserDetailsService;
import com.assignment.blogapi.security.JwtUtil;
import com.assignment.blogapi.dto.LoginSuccessResponse;
import com.assignment.blogapi.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final BlogUserService blogUserService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          BlogUserService blogUserService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.blogUserService = blogUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<LoginSuccessResponse>(new LoginSuccessResponse(jwt), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<BlogUserDto> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<BlogUserDto>(blogUserService.register(authenticationRequest.getUsername(), authenticationRequest.getPassword()), HttpStatus.OK);
    }
}
