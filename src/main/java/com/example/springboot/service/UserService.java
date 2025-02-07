package com.example.springboot.service;

import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user);

    void deleteUser(Long userId);
}
