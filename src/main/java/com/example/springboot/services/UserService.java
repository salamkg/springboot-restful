package com.example.springboot.services;

import com.example.springboot.models.entities.User;

import java.util.List;

public interface UserService {
//    User createUser(User user);
//    User updateUser(User user);
//    void deleteUserById(Long id);
//    List<User> getAllUsers();
//    User getUserById(Long id);

    User registerUser(User user);
    String loginUser(User user);

    User getUserById(Long id);
}
