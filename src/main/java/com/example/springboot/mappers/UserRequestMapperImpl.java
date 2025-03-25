package com.example.springboot.mappers;

import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRequestMapperImpl implements UserRequestMapper {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();
        userDto.id(user.getId());
        userDto.firstName(user.getFirstName());

        return userDto.build();
    }
}
