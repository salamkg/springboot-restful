package com.example.springboot.service.impl;

import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

//    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        //Convert UserDto into User JPA Entity
        User userEntity = UserMapper.mapToUser(userDto);
//        User userEntity = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(userEntity);

        // Convert User JPA entity to UserDto
        return UserMapper.mapToUserDto(savedUser);
//        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDto(user);
//        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::mapToUserDto)
            .collect(Collectors.toList());
//        return users.stream().map((user) -> modelMapper.map(user, UserDto.class))
//                .collect(Collectors.toList());

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User userToUpdate = userRepository.findById(userDto.getId()).get();
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setEmail(userDto.getEmail());
        User savedUser = userRepository.save(userToUpdate);

        return UserMapper.mapToUserDto(savedUser);
//        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
