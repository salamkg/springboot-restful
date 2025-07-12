package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<ProjectDto>> getAllProjects(@RequestParam(name = "page", defaultValue = "1") String page,
                                                           @RequestParam(name = "sortKey", defaultValue = "name", required = false) String sortKey,
                                                           @RequestParam(name = "sortOrder", defaultValue = "ASC", required = false) String sortOrder) {
        List<ProjectDto> projectDtoList = projectService.getAllProjects(page, sortKey, sortOrder);
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

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectRequestDto projectDto) {
        projectService.create(projectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDto);
    }

    @GetMapping("/{key}/boards/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId, @PathVariable(required = false) String key) {
        ProjectDto projectDto = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectDto);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectRequestDto projectDto) {
        projectService.editProject(id, projectDto);
        return ResponseEntity.ok().body(projectDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
