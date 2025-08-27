package com.example.springboot.controllers;

import com.example.springboot.audit.ActivityLog;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.enums.EntityType;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.enums.ActivityType;
import com.example.springboot.services.ActivityLogService;
import com.example.springboot.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Задачи", description = "Задачи проекта")
@RestController
@RequestMapping("/api/v1/projects/{projectKey}")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivityLogService activityLogService;

    @Operation(summary = "Просмотр всех задач доски")
    @GetMapping("/boards/{boardId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable String projectKey, @PathVariable Long boardId,
                                                     @RequestParam(required = false) String sort) throws IOException {
        List<TaskDto> allTasks = taskService.getAllTasks(projectKey, boardId, sort);
        return ResponseEntity.ok(allTasks);
    }

    @Operation(summary = "Все задачи")
    @GetMapping("/issues")
    public ResponseEntity<?> getProjectTasks(@PathVariable String projectKey,
                                             @RequestParam(required = false) String jql,
                                             @RequestParam(required = false) String limit,
                                             @RequestParam(required = false) String offset,
                                             @RequestParam(required = false) String fields) {
        List<TaskDto> list = taskService.getAllProjectTasks(projectKey, jql, limit, offset, fields);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Просмотр задачи")
    @GetMapping("/boards/{boardId}/tasks/{key}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String projectKey,
                                               @PathVariable Long boardId,
                                               @PathVariable String key) {
        TaskDto taskDto = taskService.getTaskByKey(key);
        return ResponseEntity.ok(taskDto);
    }

    @ActivityLog(type = ActivityType.CREATE, entity = EntityType.TASK)
    @Operation(summary = "Создание задачи")
    @PostMapping(name = "/boards/{boardId}/tasks", consumes = "multipart/form-data")
    public ResponseEntity<TaskDto> createTask(@PathVariable String projectKey,
                                              @PathVariable Long boardId,
                                              @RequestParam Long boardColumnId,
                                              @RequestParam String name,
                                              @RequestParam(required = false) String description,
                                              @RequestParam(required = false) String priority,
                                              @RequestParam(name = "ids", required = false) List<Long> ids,
                                              @RequestParam(name = "file", required = false) List<MultipartFile> files
    ) {

        TaskDto newTask = taskService.createTask(projectKey, boardId, boardColumnId, name, description, priority, ids, files);
        return ResponseEntity.ok(newTask);
    }

    @Operation(summary = "Переименовать задачу")
    @PutMapping("/boards/{boardId}/tasks/{taskId}/rename")
    public ResponseEntity<TaskDto> renameTask(@PathVariable String projectKey,
                                              @PathVariable Long boardId,
                                              @PathVariable Long taskId, @RequestParam String newName) {
        TaskDto taskDto = taskService.renameTask(taskId, newName);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Редактирование задачи")
    @PutMapping("/boards/{boardId}/tasks/{taskId}/edit")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String projectKey,
                                              @PathVariable Long boardId,
                                              @PathVariable Long taskId, @RequestBody Task task) {
        TaskDto editedTask = taskService.editTask(taskId, task);
        return ResponseEntity.ok(editedTask);
    }

    @Operation(summary = "Связать задачу")
    @PutMapping("/boards/{boardId}/{taskId}/link")
    public ResponseEntity<?> linkTask(@PathVariable String projectKey,
                                      @PathVariable Long boardId,
                                      @PathVariable Long taskId,
                                      @RequestParam String linkType,
                                      @RequestParam List<Long> taskIds) {
        taskService.linkTask(taskId, linkType, taskIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{id}/remove")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tasks/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN')") // Only for ADMIN
    public ResponseEntity<Task> deleteTasks(@RequestBody List<Long> ids) {
        taskService.deleteTasksById(ids);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Изменить статус")
    @PutMapping("/boards/{boardId}/tasks/{taskId}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable String projectKey,
                                                    @PathVariable Long boardId,
                                                    @PathVariable Long taskId,
                                                    @RequestParam(name = "name") String name) {
        TaskDto taskDto = taskService.updateTaskStatus(taskId, name);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Назначить задачу")
    @PutMapping("/boards/{boardId}/{taskId}/assign")
    public ResponseEntity<TaskDto> assign(@PathVariable(required = false) String projectKey,
                                          @PathVariable(required = false) Long boardId,
                                          @PathVariable Long taskId,
                                          @RequestParam(name = "ids", required = false) List<Long> ids) {
        taskService.updateTaskAssignees(projectKey, boardId, taskId, ids);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}/to-subtask")
    public ResponseEntity<TaskDto> changeTaskToSubtask(@PathVariable Long taskId, @RequestParam(name = "parent") Long parentTaskId) {
        TaskDto subTask = taskService.changeTaskToSubTask(taskId, parentTaskId);
        return ResponseEntity.ok(subTask);
    }

    @PostMapping("/qr")
    public ResponseEntity<?> generateQRCode(@RequestParam String text, @RequestParam String filePath, int width, int height) {
        taskService.generateQr(text, filePath, width, height);
        return ResponseEntity.ok().build();
    }

}
