package com.example.springboot.mappers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.entities.Project;

public interface ProjectMapper {

    Project toProject(ProjectRequestDto projectDto);
    ProjectDto toDTO(Project project);
}
