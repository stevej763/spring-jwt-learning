package com.example.springsecurityjwttutorial.filter;

import com.example.springsecurityjwttutorial.authentication.UserDetailsServiceImpl;
import com.example.springsecurityjwttutorial.jwt.JwtUtility;
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
        if (isValid(authHeader)) {
            String jwt = authHeader.replace("Bearer ", "");
            if(notExpired(jwt)) {
                String userName = jwtUtility.extractUsername(jwt);
                LOGGER.info("Extracted username from JWT username={}", userName);
                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    LOGGER.info("No existing security context, retrieved user details details={}", userDetails);
                    if(jwtUtility.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean notExpired(String jwt) {
        try {
            Date date = jwtUtility.extractExpiration(jwt);
            LOGGER.info("valid token token expiration date:{}", date);
            return date.after(new Date(System.currentTimeMillis()));
        } catch(Exception e) {
            LOGGER.info("caught exception when checking token is not expired");
            return false;
        }
    }

    private boolean isValid(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ");
    }
}
