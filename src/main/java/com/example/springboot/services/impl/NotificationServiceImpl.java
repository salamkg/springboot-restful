package com.example.springboot.services.impl;

import com.example.springboot.models.entities.User;
import com.example.springboot.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(User user, String title, String message) {
        System.out.printf("Notify %s: [%s] %s%n", user.getUsername(), title, message);
    }
}
