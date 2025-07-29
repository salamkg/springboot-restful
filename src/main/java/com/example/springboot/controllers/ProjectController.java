package com.example.springboot.controllers;

import com.example.springboot.audit.ActivityLog;
import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.entities.EntityType;
import com.example.springboot.models.enums.ActivityType;
import com.example.springboot.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Проекты")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Просмотр всех проектов")
    @GetMapping()
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projectDtoList = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK).body(projectDtoList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsByUserId(@PathVariable Long userId) {
        List<ProjectDto> projects = projectService.getProjectsByUserId(userId);

        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/{userId}/notCompleted")
    public ResponseEntity<List<ProjectDto>> getNotCompletedProjectsByUserId(@PathVariable Long userId) {
        List<ProjectDto> projects = projectService.getNotCompletedProjectsByUserId(userId);

        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProjectDto>> getRecentProjects(@RequestParam(value = "date", required = false) String date) {
        List<ProjectDto> projects = projectService.getRecentProjectsBySixMonth(date);

        return ResponseEntity.ok().body(projects);
    }

    @ActivityLog(type = ActivityType.CREATE, entity = EntityType.PROJECT)
    @Operation(summary = "Создание проекта")
    @PostMapping("/create")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectRequestDto dto) {
        ProjectDto projectDto = projectService.create(dto);
        return ResponseEntity.ok(projectDto);
    }

    @Operation(summary = "Просмотр проекта")
    @GetMapping("/boards/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId, @PathVariable(required = false) String key) {
        ProjectDto projectDto = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectDto);
    }

    @Operation(summary = "Редактирование проекта")
    @PutMapping("/{id}/edit")
    public ResponseEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectRequestDto projectDto) {
        projectService.editProject(id, projectDto);
        return ResponseEntity.ok().body(projectDto);
    }

    @ActivityLog(type = ActivityType.DELETE, entity = EntityType.PROJECT)
    @Operation(summary = "Удаление проекта")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Добавить пользователей в проект")
    @PutMapping("/{id}/add-people")
    public ResponseEntity<?> addPeople(@PathVariable Long id, @RequestParam String email) {
        projectService.addPeople(id, email);
        return ResponseEntity.ok().build();
    }
}
