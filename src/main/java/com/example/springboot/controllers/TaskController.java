package com.example.springboot.controllers;

import com.example.springboot.models.dto.ChangeLogDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.services.ChangeLogService;
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
@RequestMapping("/api/v1/boards")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ChangeLogService changeLogService;

    @GetMapping("{taskListId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable Long taskListId, @RequestParam(required = false) String sort) throws IOException {
        List<TaskDto> allTasks = taskService.getAllTasks(taskListId, sort);
        return ResponseEntity.ok(allTasks);
    }

    //TODO Pageable testing to be continued...
    @GetMapping("/all-tasks")
    public ResponseEntity<Page<TaskDto>> getAllTasksPage(@RequestParam String sort, Pageable pageable) {
        Page<TaskDto> allTasks = taskService.getAllTasksPage(sort, pageable);
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Создание задачи")
    @PostMapping(value = "/{boardColumnId}/create", consumes = "multipart/form-data")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long boardColumnId,
                                              @RequestParam String name,
                                              @RequestParam(required = false) String description,
                                              @RequestParam(required = false) String priority,
                                              @RequestParam(name = "ids", required = false) List<Long> ids,
                                              @RequestParam(name = "file", required = false) List<MultipartFile> files
    ) {

        TaskDto newTask = taskService.createTask(boardColumnId, name, description, priority, ids, files);
        return ResponseEntity.ok(newTask);
    }

    @Operation(summary = "Переименовать задачу")
    @PutMapping("/tasks/{taskId}/rename")
    public ResponseEntity<TaskDto> renameTask(@PathVariable Long taskId, @RequestParam String newName) {
        TaskDto taskDto = taskService.renameTask(taskId, newName);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/tasks/{taskId}/edit")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        TaskDto editedTask = taskService.editTask(taskId, task);
        return ResponseEntity.ok(editedTask);
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
    @PutMapping("/taskStatus/{taskId}/change")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long taskId,
                                                    @RequestParam(name = "status") String status) {
        TaskDto taskDto = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/tasks/{taskId}/change-assignee")
    public ResponseEntity<TaskDto> changeAssignee(@PathVariable Long taskId,
                                                  @RequestParam(name = "ids", required = false) List<Long> ids) {
        taskService.updateTaskAssignees(taskId, ids);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskId}/history")
    public ResponseEntity<List<ChangeLogDto>> getTaskHistory(@PathVariable Long taskId, @RequestParam(name = "h", defaultValue = "all") String sort) {
        List<ChangeLogDto> history = changeLogService.getTaskHistory(taskId, sort);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/tasks/{taskId}/to-subtask")
    public ResponseEntity<TaskDto> changeTaskToSubtask(@PathVariable Long taskId, @RequestParam(name = "parent") Long parentTaskId) {
        TaskDto subTask = taskService.changeTaskToSubTask(taskId, parentTaskId);
        return ResponseEntity.ok(subTask);
    }

}
