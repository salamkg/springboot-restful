package com.example.springboot.services;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.entities.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    List<ProjectDto> getProjectsByUserId(Long userId);
    List<ProjectDto> getNotCompletedProjectsByUserId(Long userId);
    List<ProjectDto> getRecentProjectsBySixMonth(String date);

    ProjectDto getProjectById(Long id);

    List<ProjectDto> getAllProjects();

    void create(ProjectRequestDto projectDto);

    void editProject(Long id, ProjectRequestDto projectDto);

    void deleteProject(Long id);

    void deleteOldProjects();

    void addPeople(Long projectId, String email);
}
