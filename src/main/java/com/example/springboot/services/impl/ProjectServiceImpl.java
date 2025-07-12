package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardRequestMapper;
import com.example.springboot.mappers.ProjectMapper;
import com.example.springboot.mappers.UserRequestMapper;
import com.example.springboot.models.dto.*;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private UserRequestMapper userRequestMapper;
    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public List<ProjectDto> getProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findProjectsByUserId(userId);

//        return projects.stream()
//                .map(project -> new ProjectDto(
//                        project.getId(),
//                        project.getTitle(),
//                        project.getDescription(),
//                        project.getTasks().stream()
//                                .map(task -> new TaskDto(
//                                        task.getId(),
//                                        task.getName(),
//                                        task.getDescription(),
//                                        task.getStatus(),
//                                        task.getComments().stream()
//                                                .map(comment -> new CommentDto(
//                                                        comment.getText()
//                                                )).toArray(CommentDto[]::new),
//                                        TaskListDto.builder().build()
//                                )).toArray(TaskDto[]::new)
//                ))
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public List<ProjectDto> getNotCompletedProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findNotCompletedProjects(userId);

//        return projects.stream()
//                .map(project -> new ProjectDto(
//                        project.getId(),
//                        project.getTitle(),
//                        project.getDescription(),
//                        project.getTasks().stream()
//                                .map(task -> new TaskDto(
//                                        task.getId(),
//                                        task.getName(),
//                                        task.getDescription(),
//                                        task.getStatus(),
//                                        task.getComments().stream()
//                                                .map(comment -> new CommentDto(
//                                                        comment.getText()
//                                                )).toArray(CommentDto[]::new),
//                                        task.getTaskList()
//                                )).toArray(TaskDto[]::new)
//                ))
//                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    @Override
    public List<ProjectDto> getRecentProjectsBySixMonth(String date) {
        LocalDate pasredDate = LocalDate.parse(date);
        return null;
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectDto projectDto = projectMapper.toDTO(project);
        return projectDto;
    }

    @Override
    public List<ProjectDto> getAllProjects(String page, String sortKey, String sortOrder) {
        List<Project> projectsList = projectRepository.findAll();
        List<ProjectDto> projectDtoList = projectsList.stream()
                .map(project -> projectMapper.toDTO(project))
                .toList();
        return projectDtoList;
    }

    @Override
    public void create(ProjectRequestDto projectDto) {
        Project project = projectMapper.toProject(projectDto);
        projectRepository.save(project);
    }

    @Override
    public void editProject(Long id, ProjectRequestDto projectDto) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if(projectDto.getKey() != null) {
            project.setKey(projectDto.getKey());
        }
        if(projectDto.getName() != null) {
            project.setKey(projectDto.getKey());
        }
        if(projectDto.getType() != null) {
            project.setType(projectDto.getType());
        }
        if(projectDto.getLead() != null) {
            project.setLead(userRequestMapper.toUser(projectDto.getLead()));
        }
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setIsDeleted(true);
        project.setIsDeletedAt(LocalDateTime.now());
        projectRepository.save(project);
    }

    @Override
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteOldProjects() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(2);
        List<Project> toDelete = projectRepository.findByIsDeletedTrueAndIsDeletedAtBefore(cutoffDate);
        projectRepository.deleteAll(toDelete);
    }
}
