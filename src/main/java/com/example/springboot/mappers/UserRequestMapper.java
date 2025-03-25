package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.User;

public interface UserRequestMapper {

    UserDto toUserDto(User user);
}
