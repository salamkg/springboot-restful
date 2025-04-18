package com.example.springboot.controllers;

import com.example.springboot.models.dto.ChangeLogDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.services.ChangeLogService;
import com.example.springboot.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ChangeLogService changeLogService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam String sort) throws IOException {
        List<TaskDto> allTasks = taskService.getAllTasks(sort);
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping(value = "/{taskListId}/task/create", consumes = "multipart/form-data")
    public ResponseEntity<TaskDto> createTask(@RequestParam String name,
                                              @RequestParam String description,
                                              @RequestParam String priority,
                                              @PathVariable Long taskListId,
                                              @RequestParam(name = "ids", required = false) List<Long> ids,
                                              @RequestParam(name = "file", required = false) List<MultipartFile> files
    ) {

        TaskDto newTask = taskService.createTask(name, description, priority, taskListId, ids, files);
        return ResponseEntity.ok(newTask);
    }

    @PutMapping(path = "/task/{taskId}/edit")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        TaskDto editedTask = taskService.editTask(taskId, task);
        return ResponseEntity.ok(editedTask);
    }

    @DeleteMapping(path = "/tasks/{id}/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN')") // Only for ADMIN
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/tasks/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN')") // Only for ADMIN
    public ResponseEntity<Task> deleteTasks(@RequestBody List<Long> ids) {
        taskService.deleteTasksById(ids);
        return ResponseEntity.noContent().build();
    }

    // Edit Task Position
    @PutMapping("/tasks/{taskId}/order")
    public ResponseEntity<TaskDto> updateTaskPosition(@PathVariable Long taskId, @RequestParam(name = "newPosition") Integer newPosition) {
        taskService.updateTaskPosition(taskId, newPosition);

        return new ResponseEntity<>(HttpStatus.OK);
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
