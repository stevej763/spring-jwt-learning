package com.example.springsecurityjwttutorial.authentication;

import com.example.springsecurityjwttutorial.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            PersistedUser user = userRepository.findByUserName(username);
            LOGGER.info("Found existing user in repository for userName={}", username);
            return new User(user.getUserName(), user.getPassword(), getGrantedAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Incorrect username or password", e.getCause());
        }

    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(PersistedUser user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> role.getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.name()))));
        return authorities;
    }
}
