package com.example.springboot.controllers;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam String sort) {
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
                                              @PathVariable Long taskListId, @RequestParam(name = "file") List<MultipartFile> files) {

        TaskDto newTask = taskService.createTask(name, description, priority, taskListId, files);
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

}
