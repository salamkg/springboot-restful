package com.example.springboot.services.impl;

import com.example.springboot.models.entities.UserRole;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;


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
        return userRepository.findById(id).get();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .map(user -> new org.springframework.security.core.userdetails.User(
//                      user.getUsername(),
//                      user.getPassword(),
//                        Collections.singleton(user.getRole())
//                ))
//                .orElseThrow(() -> new UsernameNotFoundException("Failed"));
//    }
}
