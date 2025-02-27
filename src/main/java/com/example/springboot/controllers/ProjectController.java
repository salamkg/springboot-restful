package com.example.springboot.controllers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.entities.Project;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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

    // localhost:8080/api/projects/recent?date=2025-01-26
    @GetMapping("/recent")
    public ResponseEntity<List<ProjectDto>> getRecentProjects(@RequestParam(value = "date", required = false) String date) {
        List<ProjectDto> projects = projectService.getRecentProjectsBySixMonth(date);

        return ResponseEntity.ok().body(projects);
    }
}
