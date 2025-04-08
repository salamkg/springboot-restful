package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.*;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        if (files != null && !files.isEmpty()) {
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
        }
        taskRepository.save(newTask);
        saveChangeLog(newTask, null,"create");

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

        saveChangeLog(task, existingTask,"edit");
        if (updated) {
            existingTask = taskRepository.save(existingTask);
        }

        return taskRequestMapper.toTaskDto(existingTask);
    }

    private void saveChangeLog(Task newTask, Task oldTask, String action) {
        //TODO Improve to save only edited values
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(newTask.getId());
        changeLog.setEntity("Task");
        changeLog.setChangedBy(newTask.getAuthor().getUsername());
        changeLog.setAction(action);
        changeLog.setUpdated_at(new Date());

        boolean isChanged = false;
        if ("edit".equals(action) || "create".equals(action) || "delete".equals(action)) {
            changeLog.setEntityName(newTask.getName());
            changeLog.setEntityDescription(newTask.getDescription());
            changeLog.setStatus(newTask.getStatus());
            changeLog.setPriority(newTask.getPriority());
            changeLog.setEntityPosition(newTask.getPosition());
            newTask.setComments(newTask.getComments());
            isChanged = true;
        } else {
            if (oldTask != null) {
                if (newTask.getName() != null && !newTask.getName().equals(oldTask.getName())) {
                    changeLog.setEntityName(newTask.getName());
                    isChanged = true;
                }
                if (newTask.getDescription() != null && !newTask.getDescription().equals(oldTask.getDescription())) {
                    changeLog.setEntityDescription(newTask.getDescription());
                    isChanged = true;
                }
                if (newTask.getStatus() != null && !newTask.getStatus().equals(oldTask.getStatus())) {
                    changeLog.setStatus(newTask.getStatus());
                    isChanged = true;
                }
                if (newTask.getPriority() != null && !newTask.getPriority().equals(oldTask.getPriority())) {
                    changeLog.setPriority(newTask.getPriority());
                    isChanged = true;
                }
                if (newTask.getPosition() != null && !newTask.getPosition().equals(oldTask.getPosition())) {
                    changeLog.setEntityPosition(newTask.getPosition());
                    isChanged = true;
                }
                if (newTask.getComments() != null && !newTask.getComments().equals(oldTask.getComments())) {
                    newTask.setComments(newTask.getComments());
                    isChanged = true;
                }
            }
        }
        if (isChanged) {
            changeLogRepository.save(changeLog);
        }
    }

    @Override
    @Transactional
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
        saveChangeLog(taskToUpdate, null,"edit");
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
