package com.example.springsecurityjwttutorial.api;

import com.example.springsecurityjwttutorial.dto.AccountCreationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationRequest;
import com.example.springsecurityjwttutorial.dto.AuthenticationResponse;
import com.example.springsecurityjwttutorial.service.WebAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationResource {

    @Autowired
    WebAuthenticationService webAuthenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = webAuthenticationService.createAuthenticationToken(authenticationRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAccount(@RequestBody AccountCreationRequest accountCreationRequest) {
        AuthenticationResponse response = webAuthenticationService.createAccount(accountCreationRequest);
        return ResponseEntity.ok(response);
    }
}
