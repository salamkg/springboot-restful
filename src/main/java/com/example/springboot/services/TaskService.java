package com.example.springboot.services;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface TaskService {

    TaskDto createTask(String projectKey, Long boardId, Long boardColumnId, String name, String description, String priority, List<Long> ids,List<MultipartFile> files);
    TaskDto editTask(Long taskId, Task task);
    void deleteTaskById(Long id);
    void deleteTasksById(List<Long> id);
    List<TaskDto> getAllTasks(String projectKey, Long boardId, String sort) throws IOException;
    Page<TaskDto> getAllTasksPage(String sort, Pageable pageable);
    TaskDto getTaskByKey(String key);
    TaskDto updateTaskStatus(Long taskId, String name);
    void updateTaskAssignees(String projectKey, Long boardId, Long taskId, List<Long> assigneeIds);
    TaskDto changeTaskToSubTask(Long taskId, Long parentTaskId);

    TaskDto renameTask(Long taskId, String newName);

    void linkTask(Long taskId, String linkType, List<Long> taskIds);
}
