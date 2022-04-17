package com.example.springsecurityjwttutorial.authentication;

import com.example.springsecurityjwttutorial.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<PersistedUser> existingUser = Optional.of(userRepository.findByUserName(username));
            if (existingUser.isPresent()) {
                PersistedUser user = existingUser.get();
                LOGGER.info("Found existing user in repository for userName={}", username);
                return new User(user.getUserName(), user.getPassword(), emptyList());
            } else {
                throw new UsernameNotFoundException("Incorrect username or password");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Incorrect username or password", e.getCause());
        }

    }
}
