package com.assignment.blogapi.security;

import com.assignment.blogapi.model.BlogUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String SECRET_KEY = "5mLpwckAJIybuxA8dMDco4bXGZ7dg8DPSoyOeDne1WZENSoQmiM5CN7XUHygy1TlB68H9wDFHDQfjHY5HYb2pTiAw00XDvHhUzKgVZSagUDzVr7tyID3J4QhniCgpt4HNBaN0P8Gz4q26yPI7UhHWRk76fkQ9Asz";
    SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String extractUserUuid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(BlogUser blogUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", blogUser.getUuid());
        claims.put("authorities", blogUser.getAuthorities());
        return createToken(claims, blogUser.getUuid().toString());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(signingKey).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            if (!isTokenExpired(token)) {
                return true;
            }
        } catch (SignatureException ex){
            logger.error("Invalid JWT Signature");
        } catch (MalformedJwtException ex){
            logger.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex){
            logger.error("Expired JWT Token");
        } catch (UnsupportedJwtException ex){
            logger.error("Unsupported JWT Token");
        } catch (IllegalArgumentException ex){
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
