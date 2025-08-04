package com.example.springboot.mappers;

import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapperImpl implements UserRequestMapper {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .username(userDto.getUsername())
                .age(userDto.getAge())
                .build();
    }

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();
        userDto.id(user.getId());
        userDto.username(user.getUsername());
        userDto.firstName(user.getFirstName());
        userDto.lastName(user.getLastName());
        userDto.age(user.getAge());
        userDto.avatar(user.getAvatar());

        return userDto.build();
    }
}
