package com.example.springboot.services;

import com.example.springboot.models.entities.Task;
import org.springframework.stereotype.Service;

@Service
public interface ChangeLogService {
    void saveChangeLog(Task newTask, Task oldTask, String action);
}
