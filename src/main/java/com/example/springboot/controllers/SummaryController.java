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
import java.util.Map;

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

    @Operation(summary = "Сводка")
    @GetMapping("/{projectKey}/summary")
    public ResponseEntity<?> getProjectTasks(@PathVariable String projectKey,
                                             @RequestParam(required = false) String filter) {
        Map<String, List<TaskDto>> list = taskService.getTasksSummary(filter);
        return ResponseEntity.ok(list);
    }
}
