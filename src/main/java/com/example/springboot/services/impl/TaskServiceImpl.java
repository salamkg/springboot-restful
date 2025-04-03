package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.*;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private TaskRequestMapper taskRequestMapper;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager em;

    @Override
    public TaskDto createTask(String name, String description, String priority, Long taskListId, List<MultipartFile> files) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setStatus(TaskStatus.NEW);
        newTask.setPosition(1);
        newTask.setPriority(priority);
        newTask.setBoard(taskList.getBoard());
        newTask.setTaskList(taskList);

        List<Attachment> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = new Attachment();
            attachment.setTask(newTask);
            attachment.setFilename(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFilePath(fileStorageService.storeFile(file));
            fileList.add(attachment);
        }
        newTask.setAttachedFiles(fileList);
        taskRepository.save(newTask);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(newTask.getId());
        changeLog.setEntity("Task");
        changeLog.setAction("create");
        changeLog.setChangedBy(firstName);
        changeLog.setEntityName(newTask.getName());
        changeLog.setEntityDescription(newTask.getDescription());
        changeLog.setEntityPosition(newTask.getPosition());
        changeLog.setPriority(newTask.getPriority());
        changeLog.setStatus(newTask.getStatus());
        changeLog.setCreated_at(new Date());
        changeLogRepository.save(changeLog);

        return taskRequestMapper.toTaskDto(newTask);
    }

    @Override
    public TaskDto editTask(Long taskId, Task task) {
        Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
        TaskList taskList = taskListRepository.findByTasks_Id(taskId)
                .orElseThrow(() -> new EntityNotFoundException("TaskList for Task with ID " + taskId + " not found"));

        boolean updated = false;
        if (task.getName() != null && !task.getName().equals(existingTask.getName())) {
            existingTask.setName(task.getName());
            updated = true;
        }
        if (task.getDescription() != null && !task.getDescription().equals(existingTask.getDescription())) {
            existingTask.setDescription(task.getDescription());
            updated = true;
        }
        if (task.getStatus() != null && !task.getStatus().equals(existingTask.getStatus())) {
            existingTask.setStatus(task.getStatus());
            updated = true;
        }
        if (task.getPriority() != null && !task.getPriority().equals(existingTask.getPriority())) {
            existingTask.setPriority(task.getPriority());
            updated = true;
        }
        if (task.getComments() != null && !task.getComments().equals(existingTask.getComments())) {
            existingTask.setComments(task.getComments());
            updated = true;
        }

        saveChangeLog(existingTask);
        if (updated) {
            existingTask = taskRepository.save(existingTask);
        }

        return taskRequestMapper.toTaskDto(existingTask);
    }

    private void saveChangeLog(Task existingTask) {
        //TODO Improve to save only edited values
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();

        changeLog.setEntityId(existingTask.getId());
        changeLog.setEntity("Task");
        changeLog.setChangedBy(firstName);
        changeLog.setAction("edit");
        changeLog.setEntityName(existingTask.getName());
        changeLog.setEntityDescription(existingTask.getDescription());
        changeLog.setEntityPosition(existingTask.getPosition());
        changeLog.setPriority(existingTask.getPriority());
        changeLog.setStatus(existingTask.getStatus());
        changeLog.setUpdated_at(new Date());
        changeLogRepository.save(changeLog);
    }

    @Override
    public void updateTaskPosition(Long taskId, Integer newPosition) {
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
        taskRepository.save(taskToUpdate);
        saveChangeLog(taskToUpdate);
    }

    @Override
    public void deleteTaskById(Long id) {
        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(id);
        changeLog.setEntity("Task");
        changeLog.setChangedBy(firstName);
        changeLog.setAction("delete");
        changeLog.setUpdated_at(new Date());
        changeLogRepository.save(changeLog);

        taskRepository.deleteById(id);
    }

    @Override
    public void deleteTasksById(List<Long> ids) {
        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        for (Long id: ids) {
            ChangeLog changeLog = new ChangeLog();
            changeLog.setEntityId(id);
            changeLog.setEntity("Task");
            changeLog.setChangedBy(firstName);
            changeLog.setAction("delete");
            changeLog.setUpdated_at(new Date());
            changeLogRepository.save(changeLog);

            taskRepository.deleteById(id);
        }

    }

    @Override
    public List<TaskDto> getAllTasks(String sort) {
        List<TaskDto> tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, sort)).stream()
                .map(task -> taskRequestMapper.toTaskDto(task))
                .collect(Collectors.toList());
        return tasks;
//        return taskRepository.findAll().stream()
//                .map(task -> taskRequestMapper.toTaskDto(task))
//                .collect(Collectors.toList());
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
                task.setStatus(TaskStatus.TESTING);
                break;
            case 4:
                task.setStatus(TaskStatus.COMPLETED);
                break;
            default:
                task.setStatus(task.getStatus());
        }
    }
}
