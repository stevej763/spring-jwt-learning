package com.example.springsecurityjwttutorial.api;

import com.example.springsecurityjwttutorial.dto.AccountCreationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationResponse;
import com.example.springsecurityjwttutorial.service.WebAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = AuthenticationResource.API_AUTHENTICATION_URL)
public class AuthenticationResource {

    public static final String API_AUTHENTICATION_URL = "/api/auth";
    @Autowired
    WebAuthenticationService webAuthenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = webAuthenticationService.createAuthenticationToken(authenticationRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refresh() {
        return ResponseEntity.ok(new AuthenticationResponse(true, "123", "321", null));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAccount(@RequestBody AccountCreationRequest accountCreationRequest) {
        AuthenticationResponse response = webAuthenticationService.createAccount(accountCreationRequest);
        return ResponseEntity.ok(response);
    }
}
