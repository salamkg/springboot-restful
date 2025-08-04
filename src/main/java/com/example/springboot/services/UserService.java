package com.example.springboot.services;

import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    String getCurrentUser();
    UserDetails loadUserByUsername(String username);
    User registerUser(User user);
    String loginUser(User user);

    User getUserById(Long id);

    User findByUsername(String username);

    UserDto getUserDetails();

    Long getCurrentUserId();

    UserDto addCoverImage(Long id, MultipartFile file);
}
