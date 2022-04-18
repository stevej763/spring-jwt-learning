package com.example.springsecurityjwttutorial.api;

import com.example.springsecurityjwttutorial.authentication.PersistedUser;
import com.example.springsecurityjwttutorial.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = SecureResource.API_SECURE)
public class SecureResource {

    Logger LOGGER = LoggerFactory.getLogger(SecureResource.class);


    public static final String API_SECURE = "/api/secure";

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        LOGGER.info("Secure endpoint accessed by user={} with permissions={}", principal.getName(), ((UsernamePasswordAuthenticationToken) principal).getAuthorities());
        String message = String.format("Hi %s, you are authenticated and can access secure endpoints", user.getName());
        List<PersistedUser> persistedUsers = userRepository.findAll();

        return ResponseEntity.ok(message + "users in db: " + persistedUsers);
    }
}
