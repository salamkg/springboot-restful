package com.example.springboot.controllers;

import com.example.springboot.config.JwtUtil;
import com.example.springboot.models.entities.User;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return ResponseEntity.ok(jwtUtil.generateToken(username));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
            catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на сервере");
        }
    }

//    @GetMapping()
//    public ResponseEntity<List<User>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }

    @GetMapping("/all")
    public String allUsers() {
        return "hello"; // Thymeleaf will look for a 'login.html' file
    }

}
