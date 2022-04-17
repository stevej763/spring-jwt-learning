package com.example.springsecurityjwttutorial.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecureResource {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        System.out.println(user.getName());
        String message = String.format("Hi %s, you are authenticated and can access secure endpoints", user.getName());
        return ResponseEntity.ok(message);
    }

}
