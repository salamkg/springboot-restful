package com.example.springboot.services;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService {

    TaskDto createTask(String name, String description, String priority, Long taskListId, List<MultipartFile> files);
    TaskDto editTask(Long taskId, Task task);
    void deleteTaskById(Long id);
    void deleteTasksById(List<Long> id);
    List<TaskDto> getAllTasks(String sort);
    TaskDto getTaskById(Long id);
    void updateTaskPosition(Long taskId, Integer position);
}
