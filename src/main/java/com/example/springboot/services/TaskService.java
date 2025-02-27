package com.example.springboot.services;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService {

    TaskDto createTask(Task task /*MultipartFile file*/);
    Task updateTask(Task task);
    void deleteTaskById(Long id);
    List<TaskDto> getAllTasks();
    TaskDto getTaskById(Long id);
    void updateTaskPosition(Long taskId, Integer position);
}
