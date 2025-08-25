package com.example.springboot.controllers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Сводная информация")
@RestController
@RequestMapping("/api/v1/projects")
public class SummaryController {

    private final ProjectService projectService;

    public SummaryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Список проектов с фильтрацией")
    @GetMapping("/{projectKey}/summary")
    public ResponseEntity<List<ProjectDto>> getProjects(@PathVariable String projectKey,
                                                        @RequestParam(required = false) String filter) {
        List<ProjectDto> list = projectService.getAllProjects();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
