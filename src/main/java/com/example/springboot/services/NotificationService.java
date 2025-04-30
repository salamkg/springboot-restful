package com.example.springboot.services;

import com.example.springboot.models.entities.User;

public interface NotificationService {
    void sendNotification(User user, String title, String message);
}
