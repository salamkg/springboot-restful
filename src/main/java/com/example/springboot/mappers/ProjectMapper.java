package com.example.springboot.mappers;

import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.entities.Project;

public interface ProjectMapper {

    ProjectDto toDTO(Project project);
}
