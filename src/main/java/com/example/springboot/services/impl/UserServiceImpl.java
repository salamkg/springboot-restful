package com.example.springboot.services.impl;

import com.example.springboot.models.enums.UserRole;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    @Override
    public String loginUser(User user) {
        return "";
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Получаем имя пользователя
        }
        throw new RuntimeException("User not authenticated!");
    }
}
