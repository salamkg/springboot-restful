package com.example.springboot.mappers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    @Override
    public ProjectDto toDTO(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .build();
    }
}
