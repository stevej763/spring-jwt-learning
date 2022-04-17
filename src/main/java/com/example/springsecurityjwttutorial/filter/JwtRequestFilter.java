package com.example.springsecurityjwttutorial.filter;

import com.example.springsecurityjwttutorial.authentication.UserDetailsServiceImpl;
import com.example.springsecurityjwttutorial.jwt.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtility jwtUtility;

    @Autowired
    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService, JwtUtility jwtUtility) {
        this.userDetailsService = userDetailsService;
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (headerIsValid(authHeader)) {
            String jwt = extractJwt(authHeader);
            if (jwtInDate(jwt, request)) {
                String userName = extractUserName(jwt);
                if (unauthenticatedUserExists(userName)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    if (jwtMatches(jwt, userDetails)) {
                        authenticateUser(request, userDetails);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean headerIsValid(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ");
    }

    private String extractJwt(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }

    private boolean jwtInDate(String jwt, HttpServletRequest request) {
        try {
            Date date = jwtUtility.extractExpiration(jwt);
            return date.after(new Date(System.currentTimeMillis()));
        } catch (ExpiredJwtException e) {
            LOGGER.info("caught exception when checking token is not expired e={}", e.getMessage());
            request.setAttribute("expired", e.getMessage());
            return false;
        }
    }

    private String extractUserName(String jwt) {
        String userName = jwtUtility.extractUsername(jwt);
        LOGGER.info("Extracted username from JWT username={}", userName);
        return userName;
    }

    private boolean unauthenticatedUserExists(String userName) {
        return userName != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean jwtMatches(String jwt, UserDetails userDetails) {
        return jwtUtility.validateToken(jwt, userDetails);
    }

    private void authenticateUser(HttpServletRequest request, UserDetails userDetails) {
        LOGGER.info("authenticating user={} with granted authorities={}", userDetails.getUsername(), userDetails.getAuthorities());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
