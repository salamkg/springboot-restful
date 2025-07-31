package com.example.springboot.services;

import com.example.springboot.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
//    User createUser(User user);
//    User updateUser(User user);
//    void deleteUserById(Long id);
    List<User> getAllUsers();
    String getCurrentUser();
    UserDetails loadUserByUsername(String username);
    User registerUser(User user);
    String loginUser(User user);

    User getUserById(Long id);

    User findByUsername(String username);
}
