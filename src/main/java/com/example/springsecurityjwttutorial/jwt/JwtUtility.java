package com.example.springsecurityjwttutorial.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Service
public class JwtUtility {

    private static final String SECRET_KEY = "SECRET_KEY";
    private static final long REFRESH_TOKEN_EXPIRY_TIME = 1000L * 60 * 60 * 24 * 365;
    private static final int ACCESS_TOKEN_EXPIRY_TIME = 1000 * 60;
    public static final String AUTHORITIES_KEY = "authorities";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        return Arrays.stream(extractAllClaims(token).get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put(AUTHORITIES_KEY, authorities);
        return createToken(claims, userDetails.getUsername(), new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRY_TIME));
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), new Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRY_TIME));
    }

    private String createToken(Map<String, Object> claims, String subject, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(expiration)
                .setIssuer("myApp")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
