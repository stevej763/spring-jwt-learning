package com.example.springsecurityjwttutorial.api;

import com.example.springsecurityjwttutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecureResource {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        System.out.println(user.getName());
        String message = String.format("Hi %s, you are authenticated and can access secure endpoints", user.getName());
        return ResponseEntity.ok(message + "users in db: " + userRepository.findAll());
    }

}
