package com.example.springsecurityjwttutorial.service;

import com.example.springsecurityjwttutorial.authentication.PersistedUser;
import com.example.springsecurityjwttutorial.authentication.Role;
import com.example.springsecurityjwttutorial.authentication.UserDetailsServiceImpl;
import com.example.springsecurityjwttutorial.dto.AccountCreationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationResponse;
import com.example.springsecurityjwttutorial.jwt.JwtUtility;
import com.example.springsecurityjwttutorial.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebAuthenticationService {

    Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public WebAuthenticationService(AuthenticationManager authenticationManager,
                                    UserDetailsServiceImpl userDetailsService,
                                    JwtUtility jwtUtility,
                                    UserRepository userRepository,
                                    PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public AuthenticationResponse createAccount(AccountCreationRequest accountCreationRequest) {
        if (!userExists(accountCreationRequest.getUsername())) {
            LOGGER.info("no existing user found!");
            PersistedUser createdUser = persistUserData(accountCreationRequest);
            AuthenticationRequest request = new AuthenticationRequest(createdUser.getUserName(), accountCreationRequest.getPassword());
            return createAuthenticationToken(request);
        } else {
            return new AuthenticationResponse(false, null, null, "Account exists");
        }
    }

    private boolean userExists(String username) {
        return userRepository.findByUserName(username) != null;
    }

    private PersistedUser persistUserData(AccountCreationRequest accountCreationRequest) {

        PersistedUser user = new PersistedUser(
                UUID.randomUUID(),
                accountCreationRequest.getUsername(),
                encoder.encode(accountCreationRequest.getPassword()),
                List.of(Role.USER));
        PersistedUser persistedUser = userRepository.save(user);
        LOGGER.info("saved new user to userRepository user={}", persistedUser);
        return persistedUser;
    }

    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest request) {
        if (userExists(request.getUsername())) {
            try {
                authenticateUser(request);
            } catch (AuthenticationException exception) {
                LOGGER.info("caught authentication exception={}", exception.toString());
                return new AuthenticationResponse(false, null, null, "problem authenticating");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String accessToken = generateAccessToken(userDetails);
            String refreshToken = generateRefreshToken(userDetails);
            
            LOGGER.info("user={} authorities={}", userDetails.getUsername(), userDetails.getAuthorities());
            return new AuthenticationResponse(true, accessToken, refreshToken, null);
        } else {
            return new AuthenticationResponse(false, null, null, "Username or password incorrect");
        }
    }

    private String generateRefreshToken(UserDetails userDetails) {
        String jwt = jwtUtility.generateRefreshToken(userDetails);
        LOGGER.info("new refresh token generated for user={} jwt={}", userDetails.getUsername(), jwt);
        return jwt;
    }

    private String generateAccessToken(UserDetails userDetails) {
        String jwt = jwtUtility.generateAccessToken(userDetails);
        LOGGER.info("new access token generated for user={} jwt={}", userDetails.getUsername(), jwt);
        return jwt;
    }

    private void authenticateUser(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword());
        authenticationManager.authenticate(authentication);
        LOGGER.info("principle={} has been authenticated", authentication.getPrincipal());
    }
}
