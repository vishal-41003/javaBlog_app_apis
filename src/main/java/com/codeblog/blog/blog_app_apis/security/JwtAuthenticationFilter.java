package com.codeblog.blog.blog_app_apis.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        log.debug("Incoming request path: {}", path);

        // Skip JWT validation for login and register APIs
        if (path.contains("/api/v1/auth")) {
            log.debug("Skipping JWT validation for auth endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String requestToken = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Check Bearer Token
        if (requestToken != null && requestToken.startsWith("Bearer ")) {

            token = requestToken.substring(7);
            log.debug("JWT token found in Authorization header");

            try {
                username = this.jwtTokenHelper.getUserNameFromToken(token);
                log.debug("Extracted username from token: {}", username);

            } catch (Exception e) {

                log.error("Invalid or expired JWT token: {}", e.getMessage());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                response.getWriter().write(
                        "{\"message\": \"Invalid or Expired JWT Token\"}"
                );

                return;
            }

        } else {

            log.debug("Authorization header missing or does not contain Bearer token");
            filterChain.doFilter(request, response);
            return;
        }

        // Validate token
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            log.debug("Loading user details for username: {}", username);

            UserDetails userDetails =
                    this.userDetailsService.loadUserByUsername(username);

            if (this.jwtTokenHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("JWT authentication successful for user: {}", username);

            } else {
                log.warn("JWT token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}