package com.example.springboot.mappers;

import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.User;


public interface UserRequestMapper {

    User toUser(UserDto userDto);
    UserDto toUserDto(User user);
}
