package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.AttachmentRepository;
import com.example.springboot.repositories.TaskListRepository;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private TaskRequestMapper taskRequestMapper;

    @Override
    public TaskDto createTask(Task task/*MultipartFile file*/) {

//        String fileUrl = fileStorageService.storeFile(file);

//        Attachment attachment = new Attachment();
//        attachment.setFilename(file.getOriginalFilename());
//        attachment.setFileType(file.getContentType());
//        attachment.setFilePath(fileUrl);
//        attachment.setTask(task);

//        attachmentRepository.save(attachment);

//        task.setAttachedFiles(Collections.singletonList(attachment));

        // Найдем список, к которому принадлежит карточка
        TaskList taskList = taskListRepository.findById(task.getTaskList().getId())
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        // Установим ссылку на список
        task.setTaskList(taskList);

        taskRepository.save(task);
        return taskRequestMapper.toTaskDto(task);
    }

    @Override
    public Task updateTask(Task task) {
        taskRepository.save(task);
        return task;
    }

    @Override
    public void updateTaskPosition(Long taskId, Integer newPosition) {

        // Найдем список, к которому принадлежит задача
        TaskList taskList = taskListRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        // Текущая задача
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        // Получаем текущую позицию задачи
        Integer currentPosition = taskToUpdate.getPosition();
        if (!currentPosition.equals(newPosition)) {
            if (newPosition > currentPosition) {
                taskToUpdate.setPosition(newPosition);
            } else {
                taskToUpdate.setPosition(currentPosition);
            }
        }

        setTaskStatusByPosition(newPosition, taskToUpdate);
        taskToUpdate.setPosition(newPosition);

        taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> taskRequestMapper.toTaskDto(task))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskRequestMapper.toTaskDto(task);
    }

    public void setTaskStatusByPosition(Integer position, Task task) {
        switch (position) {
            case 1:
                task.setStatus(TaskStatus.NEW);
                break;
            case 2:
                task.setStatus(TaskStatus.PENDING);
                break;
            case 3:
                task.setStatus(TaskStatus.COMPLETED);
                break;
            default:
                task.setStatus(task.getStatus());
        }
    }
}
