package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean read = false;

    public enum NotificationType {
        NEW_TASK, COMMENT_ADDED, TASK_COMPLETED, TASK_ASSIGNED
    }
}
