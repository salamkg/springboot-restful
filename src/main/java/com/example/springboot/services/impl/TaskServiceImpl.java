package com.example.springboot.services.impl;

import com.example.springboot.audit.TelegramService;
import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.models.enums.TaskStatus;
import com.example.springboot.repositories.*;
import com.example.springboot.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private BoardColumnRepository boardColumnRepository;
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
    @Autowired
    private NotificationService notificationService;

    @Override
    public TaskDto createTask(Long boardColumnId, String name, String description, String priority, List<Long> ids, List<MultipartFile> files) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new RuntimeException("BoardColumn Not Found"));

        Task newTask = Task.builder()
                .name(name)
                .description(description)
                .status(TaskStatus.NEW)
                .position(1)
                .priority(priority)
                .boardColumn(boardColumn)
                .build();

        if (ids != null && !ids.isEmpty()) {
            List<User> assignees = userRepository.findAllById(ids)
                    .stream()
                    .map(user -> userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User Not Found")))
                    .toList();
            newTask.setAssignedUsers(assignees);
        }

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
        //TODO notification to be continued...
        if (newTask.getAssignedUsers() != null) {
            for (User user : newTask.getAssignedUsers()) {
                notificationService.sendNotification(
                        user,
                        "New task assigned: " + newTask.getName(),
                        "You are assigned as task performer"
                );
            }
        }
//        changeLogService.saveChangeLog(newTask,null,"create");

        return taskRequestMapper.toTaskDto(newTask);
    }

    @Override
    public TaskDto editTask(Long taskId, Task task) {
        Task editTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));

        boolean updated = false;
        if (task.getName() != null && !task.getName().equals(editTask.getName())) {
            editTask.setName(task.getName());
            updated = true;
        }
        if (task.getDescription() != null && !task.getDescription().equals(editTask.getDescription())) {
            editTask.setDescription(task.getDescription());
            updated = true;
        }
        if (task.getStatus() != null && !task.getStatus().equals(editTask.getStatus())) {
            editTask.setStatus(task.getStatus());
            updated = true;
        }
        if (task.getPriority() != null && !task.getPriority().equals(editTask.getPriority())) {
            editTask.setPriority(task.getPriority());
            updated = true;
        }
        if (task.getComments() != null && !task.getComments().equals(editTask.getComments())) {
            editTask.setComments(task.getComments());
            updated = true;
        }

        if (updated) {
            editTask = taskRepository.save(editTask);
        }

        return taskRequestMapper.toTaskDto(editTask);
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
    public TaskDto renameTask(Long taskId, String newName) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        if (newName != null && !newName.equals(task.getName())) {
            task.setName(newName);
            taskRepository.save(task);
        }
        return taskRequestMapper.toTaskDto(task);
    }

    @Override
    public void updateTaskStatus(Long taskId, String status) {
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        BoardColumn boardColumn = boardColumnRepository.findBoardColumnByName(status);

        taskToUpdate.setBoardColumn(boardColumn);
        taskRepository.save(taskToUpdate);
//        changeLogService.saveChangeLog(taskToUpdate, null,"edit");
    }

    @Override
    public void deleteTaskById(Long id) {
        //Logging
//        String firstName;
//        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteTasksById(List<Long> ids) {
        //Logging
//        String firstName;
//        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        for (Long id: ids) {
            taskRepository.deleteById(id);
        }
    }

    @Override
    public List<TaskDto> getAllTasks(Long taskListId, String sort) throws IOException {
        List<TaskDto> taskDtoList = taskRepository.findAllByBoardColumn_Id(taskListId).stream()
                .map(task -> taskRequestMapper.toTaskDto(task))
                .toList();
        return taskDtoList;

//        List<TaskDto> tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, sort)).stream()
//                .map(task -> taskRequestMapper.toTaskDto(task))
//                .collect(Collectors.toList());
//        telegramService.send("Hello! This is message from Spring to Telegram");
//        return tasks;
    }

    @Override
    public Page<TaskDto> getAllTasksPage(String sort, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(task -> taskRequestMapper.toTaskDto(task));
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        Task taskWithComments = taskRepository.findByIdWithComments(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskRequestMapper.toTaskDto(taskWithComments);
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
