package com.example.springboot.services.impl;

import com.example.springboot.mappers.UserRequestMapper;
import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.UserAttachment;
import com.example.springboot.models.enums.UserRole;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.UserAttachmentRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private UserRequestMapper userRequestMapper;
    @Autowired
    private UserAttachmentRepository userAttachmentRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    @Override
    public String loginUser(User user) {
        return "";
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDto getUserDetails() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Получаем имя пользователя
        }
        throw new RuntimeException("User not authenticated!");
    }

    public Long getCurrentUserId() {
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
                String username = springUser.getUsername();
                User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
                userId = user.getId();
            }
        }
        return userId;
    }

    @Override
    public UserDto addCoverImage(Long id, MultipartFile coverImage) {
        User user = getUserById(id);

        // 1. Generate unique filename
        String filename = coverImage.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID() + extension;

        // 2. File path
        String uploadDir = System.getProperty("user.dir") + "/uploads/avatars/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destinationFile = new File(uploadDir + uniqueFilename);
        System.out.println("Current working dir: " + destinationFile.getAbsolutePath());

        // 3. Store file
        try {
            coverImage.transferTo(destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error trying to upload an avatar to the user: " + e.getMessage());
        }

        UserAttachment userAttachment = new UserAttachment();
        userAttachment.setFilename(filename);
        userAttachment.setFilePath(uploadDir + uniqueFilename);
        userAttachment.setUser(user);
        userAttachmentRepository.save(userAttachment);

        user.setAvatar(userAttachment.getFilePath());
        userRepository.save(user);

        return userRequestMapper.toUserDto(user);
    }
}
