package com.example.springboot.controllers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.services.ProjectService;
import com.example.springboot.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Сводная информация")
@RestController
@RequestMapping("/api/v1/projects")
public class SummaryController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public SummaryController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Operation(summary = "Все задачи")
    @GetMapping("/{projectKey}/summary")
    public ResponseEntity<?> getProjectTasks(@PathVariable String projectKey,
                                             @RequestParam(required = false) String jql,
                                             @RequestParam(required = false) String limit,
                                             @RequestParam(required = false) String offset,
                                             @RequestParam(required = false) String fields) {
        List<TaskDto> list = taskService.getTasksSummary();
        return ResponseEntity.ok(list);
    }
}
