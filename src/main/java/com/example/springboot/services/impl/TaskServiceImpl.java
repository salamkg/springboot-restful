package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.*;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
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
    public TaskDto createTask(Task task, Long taskListId, MultipartFile files) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        Task newTask = new Task();
        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(TaskStatus.NEW);
        newTask.setPosition(1);
        newTask.setPriority(task.getPriority());
        newTask.setComments(task.getComments());
        newTask.setBoard(taskList.getBoard());
        newTask.setTaskList(taskList);
        taskRepository.save(newTask);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(newTask.getId());
        changeLog.setEntity("Task");
        changeLog.setAction("create");
        changeLog.setChangedBy(firstName);
        changeLog.setOldValue("Old");
        changeLog.setNewValue("New");
        changeLog.setTimestamp(new Date());
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

        if (updated) {
            existingTask = taskRepository.save(existingTask);
        }

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(existingTask.getId());
        changeLog.setEntity("Task");
        changeLog.setChangedBy(firstName);
        changeLog.setAction("edit");
        changeLog.setOldValue("old");
        changeLog.setNewValue("new");
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        return taskRequestMapper.toTaskDto(existingTask);
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

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(taskId);
        changeLog.setEntity("Task");
        changeLog.setChangedBy(firstName);
        changeLog.setAction("edit position");
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        taskRepository.save(taskToUpdate);
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
        changeLog.setTimestamp(new Date());
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
            changeLog.setTimestamp(new Date());
            changeLogRepository.save(changeLog);

            taskRepository.deleteById(id);
        }

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
