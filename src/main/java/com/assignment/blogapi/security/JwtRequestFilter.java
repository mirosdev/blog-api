package com.assignment.blogapi.security;


import com.assignment.blogapi.dto.BlogUserDto;
import com.assignment.blogapi.model.BlogUser;
import com.assignment.blogapi.repository.BlogUserRepository;
import com.assignment.blogapi.service.BlogUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "session-token";

    private final JwtUtil jwtUtil;
    private final BlogUserRepository blogUserRepository;

    public JwtRequestFilter(JwtUtil jwtUtil, BlogUserRepository blogUserRepository) {
        this.jwtUtil = jwtUtil;
        this.blogUserRepository = blogUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Optional<Cookie> oCookieAuth = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        Cookie cookieAuth = oCookieAuth.orElse(null);

        String uuid = null;
        String jwt = null;

        if (cookieAuth != null) {
            jwt = cookieAuth.getValue();
            try {
                jwtUtil.validateToken(jwt);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            uuid = jwtUtil.extractUserUuid(cookieAuth.getValue());
        }

        if (uuid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            BlogUserDto blogUserDto;
            Optional<BlogUser> oBlogUser;
            try {
                oBlogUser = this.blogUserRepository.findById(UUID.fromString(uuid));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (oBlogUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                blogUserDto = BlogUserService.mapUserData(oBlogUser.get());
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    blogUserDto, null, blogUserDto.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }

}
