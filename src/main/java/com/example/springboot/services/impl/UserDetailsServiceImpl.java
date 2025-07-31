package com.example.springboot.services.impl;

import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.security.CustomUserDetails;
import com.example.springboot.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user); // Оборачиваем сущность User в CustomUserDetails
    }
}
