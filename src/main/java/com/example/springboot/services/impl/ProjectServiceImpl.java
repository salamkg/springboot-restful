package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardRequestMapper;
import com.example.springboot.mappers.ProjectMapper;
import com.example.springboot.mappers.UserRequestMapper;
import com.example.springboot.models.dto.*;
import com.example.springboot.models.entities.*;
import com.example.springboot.models.enums.ActivityType;
import com.example.springboot.models.enums.UserRole;
import com.example.springboot.models.enums.UserStatus;
import com.example.springboot.repositories.*;
import com.example.springboot.services.ActivityLogService;
import com.example.springboot.services.ProjectService;
import com.example.springboot.services.UserDetailsService;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardColumnRepository boardColumnRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityLogService activityLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;


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
        return projectMapper.toDTO(project);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<Project> projectsList = projectRepository.findAllNotDeleted();
        return projectsList.stream()
                .map(project -> projectMapper.toDTO(project))
                .toList();
    }

    @Override
    public ProjectDto create(ProjectRequestDto projectDto) {
        String username = userService.getCurrentUser();
        User lead = userService.findByUsername(username);

        Project project = projectMapper.toProject(projectDto);
        project.setLead(lead);
        projectRepository.save(project);

        Board projectBoard = Board.builder()
                .project(project)
                .build();
        boardRepository.save(projectBoard);

        List<BoardColumn> boardColumns = List.of(
                BoardColumn.builder()
                        .name("In Progress")
                        .board(projectBoard)
                        .build(),
                BoardColumn.builder()
                        .name("Done")
                        .board(projectBoard)
                        .build(),
                BoardColumn.builder()
                        .name("Testing")
                        .board(projectBoard)
                        .build()
        );
        boardColumnRepository.saveAll(boardColumns);
        return projectMapper.toDTO(project);
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

    @Override
    public void addPeople(Long projectId, String email) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        // TODO можно реализовать отдельную сущность Invitation для полноценной механики приглашений
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setStatus(UserStatus.INVITED);
            newUser.setPassword("password");
            newUser.setRole(UserRole.USER);
            return userRepository.save(newUser);
        });

        if(!project.getPeople().contains(user)) {
            project.getPeople().add(user);
            project.setPeople(project.getPeople().subList(0, 2));
        }
        if(!user.getProjects().contains(project)) {
            user.getProjects().add(project);
        }

        projectRepository.save(project);

    }
}
