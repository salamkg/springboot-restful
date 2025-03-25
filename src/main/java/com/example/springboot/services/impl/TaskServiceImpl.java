package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.*;
import com.example.springboot.services.FileStorageService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
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
        newTask.setStatus(task.getStatus());
        newTask.setPosition(task.getPosition());
        newTask.setComments(task.getComments());
        newTask.setBoard(taskList.getBoard());
        newTask.setTaskList(taskList);
        taskRepository.save(newTask);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        ChangeLog changeLog = new ChangeLog();
        changeLog.setTaskId(newTask.getId());
        changeLog.setAction("create");
        changeLog.setChangedBy(firstName);
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);
        return taskRequestMapper.toTaskDto(newTask);
    }

    @Override
    public TaskDto editTask(Long taskId, Task task) {
        Task newTask = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task for edit not found"));
        TaskList taskList = taskListRepository.findByTasks_Id(taskId)
                .orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(task.getStatus());
        newTask.setPosition(task.getPosition());
        newTask.setComments(task.getComments());
        newTask = taskRepository.save(newTask);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setTaskId(newTask.getId());
        changeLog.setChangedBy(firstName);
        changeLog.setAction("edit");
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        return taskRequestMapper.toTaskDto(newTask);
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
        taskToUpdate.setPosition(newPosition);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setTaskId(taskId);
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
        changeLog.setTaskId(id);
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
            changeLog.setTaskId(id);
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
                task.setStatus(TaskStatus.COMPLETED);
                break;
            default:
                task.setStatus(task.getStatus());
        }
    }
}
