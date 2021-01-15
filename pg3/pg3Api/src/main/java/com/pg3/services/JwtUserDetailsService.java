package com.pg3.services;

import java.util.ArrayList;

import com.pg3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.pg3.models.User user = userRepository.findByEmail(username);

        if (user.getEmail().equals(username)) {
            return new User(username, user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }
}