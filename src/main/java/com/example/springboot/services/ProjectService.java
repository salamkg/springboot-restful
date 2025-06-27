package com.example.springboot.services;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.entities.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    List<ProjectDto> getProjectsByUserId(Long userId);
    List<ProjectDto> getNotCompletedProjectsByUserId(Long userId);
    List<ProjectDto> getRecentProjectsBySixMonth(String date);

    Project getProjectById(Long id);

    List<ProjectDto> getAllProjects(String page, String sortKey, String sortOrder);
}
