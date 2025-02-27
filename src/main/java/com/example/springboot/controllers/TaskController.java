package com.example.springboot.controllers;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.services.TaskService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // localhost:8080/tasks
    @GetMapping()
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    // localhost:8080/tasks/{id}
    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    // localhost:8080/api/tasks
    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody Task task/*@RequestParam(value = "file", required = false) MultipartFile file*/) {

        if (task.getTaskList() == null || task.getTaskList().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskDto newTask = taskService.createTask(task);
        return ResponseEntity.ok(newTask);
    }

    // localhost:8080/tasks/1
    @PutMapping(path = "/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        Task newTask = taskService.updateTask(task);
        return ResponseEntity.ok(newTask);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    // Edit Task Position
    @PutMapping("/{taskId}/order")
    public ResponseEntity<TaskDto> updateTaskPosition(@PathVariable Long taskId, @RequestParam(name = "newPosition") Integer newPosition) {
        taskService.updateTaskPosition(taskId, newPosition);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
