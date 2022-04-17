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
        PersistedUser user = new PersistedUser(
                UUID.randomUUID(),
                accountCreationRequest.getUsername(),
                encoder.encode(accountCreationRequest.getPassword()));
        PersistedUser createdUser = userRepository.save(user);
        LOGGER.info("saved new user to userRepository user={}", createdUser);
        AuthenticationRequest request = new AuthenticationRequest(createdUser.getUserName(), accountCreationRequest.getPassword());
        return createAuthenticationToken(request);
    }

    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest request) {
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword());
            LOGGER.info("preauthentication manager for user={}", request.getUsername());

            authenticationManager.authenticate(authentication);
            LOGGER.info("user={} has been authenticated", request.getUsername());
        } catch (Exception exception) {
            LOGGER.info("caught bad credentials exception={}", exception.getCause().getMessage());
            return new AuthenticationResponse(false, null, "problem authenticating");
        }
        LOGGER.info("retrieving user details for new user");
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtility.generateToken(userDetails);
        LOGGER.info("new JWT generated for user={} jwt={}",userDetails.getUsername(), jwt);
        return new AuthenticationResponse(true, jwt, null);
    }
}
