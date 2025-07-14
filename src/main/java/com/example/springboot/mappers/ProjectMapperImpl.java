package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.entities.Project;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    private final UserRequestMapper userRequestMapper;
    private final BoardRequestMapper boardRequestMapper;

    public ProjectMapperImpl(UserRequestMapper userRequestMapper, BoardRequestMapper boardRequestMapper) {
        this.userRequestMapper = userRequestMapper;
        this.boardRequestMapper = boardRequestMapper;
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
                .boards(project.getBoards().stream().map(boardRequestMapper::toBoardDto).toList())
                .build();
    }
}
