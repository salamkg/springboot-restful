package com.example.springboot.mappers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    private final UserRequestMapper userRequestMapper;

    public ProjectMapperImpl(UserRequestMapper userRequestMapper) {
        this.userRequestMapper = userRequestMapper;
    }

    @Override
    public Project toProject(ProjectRequestDto projectDto) {
        return Project.builder()
                .name(projectDto.getName())
                .type(projectDto.getType())
                .key(projectDto.getKey())
                .isDeleted(projectDto.getIsDeleted())
                .build();
    }

    @Override
    public ProjectDto toDTO(Project project) {
        return ProjectDto.builder()
                .name(project.getName())
                .type(project.getType())
                .key(project.getKey())
                .isDeleted(project.getIsDeleted())
                .build();
    }
}
