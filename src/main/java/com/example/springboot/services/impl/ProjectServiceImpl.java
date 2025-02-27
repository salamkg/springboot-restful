package com.example.springboot.services.impl;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Project;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.CommentRepository;
import com.example.springboot.repositories.ProjectRepository;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.CommentService;
import com.example.springboot.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    @Override
    public List<ProjectDto> getProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findProjectsByUserId(userId);

        return projects.stream()
                .map(project -> new ProjectDto(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getTasks().stream()
                                .map(task -> new TaskDto(
                                        task.getId(),
                                        task.getName(),
                                        task.getDescription(),
                                        task.getStatus(),
                                        task.getComments().stream()
                                                .map(comment -> new CommentDto(
                                                        comment.getText()
                                                )).toArray(CommentDto[]::new)
                                )).toArray(TaskDto[]::new)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getNotCompletedProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findNotCompletedProjects(userId);

        return projects.stream()
                .map(project -> new ProjectDto(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getTasks().stream()
                                .map(task -> new TaskDto(
                                        task.getId(),
                                        task.getName(),
                                        task.getDescription(),
                                        task.getStatus(),
                                        task.getComments().stream()
                                                .map(comment -> new CommentDto(
                                                        comment.getText()
                                                )).toArray(CommentDto[]::new)
                                )).toArray(TaskDto[]::new)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getRecentProjectsBySixMonth(String date) {
        LocalDate pasredDate = LocalDate.parse(date);
        return projectRepository.findRecentProjects(pasredDate).stream()
                .map(project -> new ProjectDto(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        null
                )).collect(Collectors.toList());
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}
