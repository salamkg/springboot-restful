package com.example.springboot.controllers;

import com.example.springboot.mappers.UserRequestMapper;
import com.example.springboot.models.dto.ActivityLogDto;
import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.User;
import com.example.springboot.services.ActivityLogService;
import com.example.springboot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final ActivityLogService activityLogService;

    public UserController(UserService userService, UserRequestMapper userRequestMapper, ActivityLogService activityLogService) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.activityLogService = activityLogService;
    }

    @Operation(summary = "Профиль")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userRequestMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Логи пользователя")
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<ActivityLogDto>> getUserLogs(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        List<ActivityLogDto> list = activityLogService.getHistory(user.getId());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Добавить фон для профиля")
    @PutMapping(path = "/{id}/add-cover-image", consumes = "multipart/form-data")
    public ResponseEntity<UserDto> addCoverImage(@PathVariable("id") Long id, @RequestParam MultipartFile file) {
        UserDto userDto = userService.addCoverImage(id, file);
        return ResponseEntity.ok(userDto);
    }
}
