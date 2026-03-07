package com.codeblog.blog.blog_app_apis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 60 * 60;

    private String secret = "mySuperSecretJwtKeyForBlogApplication123456";

    public String getUserNameFromToken(String token) {
        log.debug("Extracting username from JWT token");
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        log.debug("Extracting expiration from JWT token");
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);

        boolean expired = expiration.before(new Date());

        if (expired) {
            log.warn("JWT token is expired");
        }

        return expired;
    }

    public String generateToken(UserDetails userDetails) {

        log.info("Generating JWT token for user: {}", userDetails.getUsername());

        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUserNameFromToken(token);

        boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        if (valid) {
            log.debug("JWT token validated successfully for user: {}", username);
        } else {
            log.warn("JWT token validation failed for user: {}", username);
        }

        return valid;
    }
}