package com.example.springboot.services.impl;

import com.example.springboot.audit.TelegramService;
import com.example.springboot.mappers.TaskRequestMapper;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.models.enums.TaskLinkType;
import com.example.springboot.models.enums.TaskStatus;
import com.example.springboot.repositories.*;
import com.example.springboot.services.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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
    private ActivityLogService activityLogService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public TaskDto createTask(String projectKey, Long boardId, Long boardColumnId, String name, String description, String priority, List<Long> ids, List<MultipartFile> files) {
        String username = userService.getCurrentUser();
        User author = userService.findByUsername(username);

        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new RuntimeException("BoardColumn Not Found"));
        Project project = board.getProject();

        //Count how many tasks have this project
        Long countTasks = taskRepository.countByProjectId(project.getId());
        String taskKey = project.getKey() + "-" + (countTasks + 1);

        Task newTask = Task.builder()
                .name(name)
                .description(description)
                .status(TaskStatus.NEW)
                .position(1)
                .key(taskKey) // PRJ-17, 18, 19
                .priority(priority)
                .board(board)
                .boardColumn(boardColumn)
                .author(author)
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
    public void updateTaskAssignees(String projectKey, Long boardId, Long taskId, List<Long> assigneeIds) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        User currentUser = userService.findByUsername(userService.getCurrentUser());

        if (assigneeIds != null && !assigneeIds.isEmpty()) {
            List<User> assignees = userRepository.findAllById(assigneeIds)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            task.setAssignedUsers(assignees);
        } else {
            if (currentUser != null) {
                List<User> list = new ArrayList<>();
                list.add(currentUser);
                task.setAssignedUsers(list);
            }
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
    public void linkTask(Long taskId, String linkType, List<Long> taskIds) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        if (linkType != null && !linkType.isEmpty()) {
            task.setDependencies(taskRepository.findAllById(taskIds));
            task.setLinkType(TaskLinkType.IS_BLOCKED_BY);
            taskRepository.save(task);
        }
    }

    @Override
    public void generateQr(String text, String filePath, int w, int h) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, w, h, hints);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TaskDto> getTasksSummary() {
        List<Task> doneList = taskRepository.findAllDoneLast7Days();
        List<Task> createdList = taskRepository.findAllCreatedLast7Days();
        List<Task> updatedList = taskRepository.findAllUpdatedLast7Days();

        return updatedList.stream().map(task -> taskRequestMapper.toTaskDto(task)).collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTaskStatus(Long taskId, String newStatusName) {
        if (taskId == null || newStatusName == null) {
            throw new IllegalArgumentException("Task ID and status name must not be null");
        }
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));

        TaskStatus newStatus;
        try {
            newStatus = TaskStatus.valueOf(newStatusName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task status: " + newStatusName);
        }
        task.setStatus(newStatus);
        taskRepository.save(task);

        return taskRequestMapper.toTaskDto(task);
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
    public List<TaskDto> getAllTasks(String projectKey, Long boardId, String sort) throws IOException {
        return taskRepository.findTasksByProjectKeyAndBoardId(projectKey, boardId).stream()
                .map(task -> taskRequestMapper.toTaskDto(task))
                .toList();
    }

    @Override
    public List<TaskDto> getAllProjectTasks(String projectKey, String jql, String limit, String offset, String fields) {
        Project project = projectRepository.findByProjectKey(projectKey).orElseThrow(() -> new EntityNotFoundException("Project Not Found"));
        List<Task> tasks = taskRepository.findAllByProject(project);
        return tasks.stream().map(task -> taskRequestMapper.toTaskDto(task)).toList();
    }

    @Override
    public TaskDto getTaskByKey(String key) {
        Task task = taskRepository.findTaskByKey(key).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        return taskRequestMapper.toTaskDto(task);
    }

    //TODO доделать
    public boolean canBeStarted(Task task) {
        List<Task> dependencies = task.getDependencies();
        for (Task dependency : dependencies) {
            if (!dependency.getStatus().equals(TaskStatus.DONE)) {
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
                task.setStatus(TaskStatus.NEW);
                break;
            case 3:
                task.setStatus(TaskStatus.TESTING);
                break;
            case 4:
                task.setStatus(TaskStatus.DONE);
                break;
            default:
                task.setStatus(task.getStatus());
        }
    }
}
