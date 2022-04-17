package com.example.springsecurityjwttutorial.service;

import com.example.springsecurityjwttutorial.authentication.PersistedUser;
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
        PersistedUser createdUser = persistUserData(accountCreationRequest);
        AuthenticationRequest request = new AuthenticationRequest(createdUser.getUserName(), accountCreationRequest.getPassword());
        return createAuthenticationToken(request);
    }

    private PersistedUser persistUserData(AccountCreationRequest accountCreationRequest) {
        PersistedUser user = new PersistedUser(
                UUID.randomUUID(),
                accountCreationRequest.getUsername(),
                encoder.encode(accountCreationRequest.getPassword()));
        PersistedUser persistedUser = userRepository.save(user);
        LOGGER.info("saved new user to userRepository user={}", persistedUser);
        return persistedUser;
    }

    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest request) {
        try {
            authenticateUser(request);
        } catch (AuthenticationException exception) {
            LOGGER.info("caught authentication exception={}", exception.toString());
            return new AuthenticationResponse(false, null, null, "problem authenticating");
        }
        String jwt = generateJwt(request);
        return new AuthenticationResponse(true, jwt, null, null);
    }

    private String generateJwt(AuthenticationRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtility.generateAccessToken(userDetails);
        LOGGER.info("new JWT generated for user={} jwt={}",userDetails.getUsername(), jwt);
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
