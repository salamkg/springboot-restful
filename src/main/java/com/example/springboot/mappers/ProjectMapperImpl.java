package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.dto.ProjectRequestDto;
import com.example.springboot.models.dto.UserDto;
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
                .id(projectDto.getId())
                .name(projectDto.getName())
                .type(projectDto.getType())
                .key(projectDto.getKey())
                .isDeleted(projectDto.getIsDeleted())
                .build();
    }

    @Override
    public ProjectDto toDTO(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .type(project.getType())
                .key(project.getKey())
                .isDeleted(project.getIsDeleted())
                .boards(project.getBoards() != null
                        ? project.getBoards().stream().map(boardRequestMapper::toBoardDto).toList()
                        : null)
                .lead(project.getLead() != null
                        ?
                        UserDto.builder()
                                .id(project.getLead().getId())
                                .username(project.getLead().getUsername())
                                .firstName(project.getLead().getFirstName())
                                .lastName(project.getLead().getLastName())
                                .build()
                        : null
                )
                .build();
    }
}
