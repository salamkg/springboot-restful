package com.example.springboot.services.impl;

import com.example.springboot.audit.TelegramService;
import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.*;
import com.example.springboot.services.ChangeLogService;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FileStorageService fileStorageService;
    @Autowired
    private TaskRequestMapper taskRequestMapper;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChangeLogService changeLogService;
    @Autowired
    private TelegramService telegramService;

    @Override
    public TaskDto createTask(String name, String description, String priority, Long taskListId, List<Long> ids, List<MultipartFile> files) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));
        User author = userRepository.findByUsername(userService.getCurrentUser())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setStatus(TaskStatus.NEW);
        newTask.setPosition(1);
        newTask.setPriority(priority);
        newTask.setAuthor(author);
        newTask.setBoard(taskList.getBoard());
        newTask.setTaskList(taskList);

        if (ids != null && !ids.isEmpty()) {
            List<User> assignees = userRepository.findAllById(ids)
                    .stream()
                    .map(user -> userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User Not Found")))
                    .toList();
            newTask.setAssignedUsers(assignees);
        }

        //Parsing text from photo
//        for (MultipartFile file : files) {
//            String result = ocrService.extractText(file);
//            System.out.println(result);
//        }

        if (files != null && !files.isEmpty()) {
            List<Attachment> fileList = files.stream()
                    .map(file -> Attachment.builder()
                            .task(newTask)
                            .filename(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .filePath(file.getOriginalFilename())
                            .build()
                    )
                    .toList();
            newTask.setAttachedFiles(fileList);
        }
        taskRepository.save(newTask);
        changeLogService.saveChangeLog(newTask,null,"create");

        return taskRequestMapper.toTaskDto(newTask);
    }

    @Override
    public TaskDto editTask(Long taskId, Task task) {
        Task editTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
        TaskList taskList = taskListRepository.findByTasks_Id(taskId)
                .orElseThrow(() -> new EntityNotFoundException("TaskList for Task with ID " + taskId + " not found"));

        changeLogService.saveChangeLog(task, existingTask,"edit");
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

        if (updated) {
            existingTask = taskRepository.save(existingTask);
        }

        return taskRequestMapper.toTaskDto(existingTask);
    }

    @Override
    public void updateTaskAssignees(Long taskId, List<Long> assigneeIds) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        if (assigneeIds != null && !assigneeIds.isEmpty()) {
            List<User> assignees = userRepository.findAllById(assigneeIds)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            task.setAssignedUsers(assignees);
        }
        taskRepository.save(task);
    }

    @Override
    public TaskDto changeTaskToSubTask(Long taskId, Long parentTaskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        Task parentTask = taskRepository.findById(parentTaskId).orElse(null);
        task.setParentTask(parentTask);
        taskRepository.save(task);
        return taskRequestMapper.toTaskDto(task);
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
        changeLogService.saveChangeLog(taskToUpdate, null,"edit");
    }

    @Override
    public void deleteTaskById(Long id) {
        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteTasksById(List<Long> ids) {
        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        for (Long id: ids) {
            taskRepository.deleteById(id);
        }
    }

    @Override
    public List<TaskDto> getAllTasks(String sort) throws IOException {
        List<TaskDto> tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, sort)).stream()
                .map(task -> taskRequestMapper.toTaskDto(task))
                .collect(Collectors.toList());
        telegramService.send("hello");
        return tasks;
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskRequestMapper.toTaskDto(task);
    }

    //TODO доделать
    public boolean canBeStarted(Task task) {
        List<Task> dependencies = task.getDependencies();
        for (Task dependency : dependencies) {
            if (!dependency.getStatus().equals(TaskStatus.COMPLETED)) {
                return false;
            }
        }
        return true;
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
