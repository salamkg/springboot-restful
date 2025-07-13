package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.repositories.BoardColumnRepository;
import org.springframework.stereotype.Component;


@Component
public class TaskRequestMapperImpl implements TaskRequestMapper {
    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnRequestMapper boardColumnRequestMapper;

    public TaskRequestMapperImpl(BoardColumnRepository boardColumnRepository, BoardColumnRequestMapper boardColumnRequestMapper) {
        this.boardColumnRepository = boardColumnRepository;
        this.boardColumnRequestMapper = boardColumnRequestMapper;
    }

    @Override
    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }

        BoardColumn boardColumn = boardColumnRepository.findById(task.getBoardColumn().getId()).orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        TaskDto.TaskDtoBuilder taskDto = TaskDto.builder();
        taskDto.id(task.getId());
        taskDto.name(task.getName());
        taskDto.description(task.getDescription());
        taskDto.priority(task.getPriority());
        if (task.getComments() != null) {
            taskDto.comments(task.getComments().stream()
                    .map(comment -> new CommentDto(
                            comment.getText()
                    )).toArray(CommentDto[]::new));
        }
        taskDto.status(task.getStatus());
        taskDto.boardColumnDTO(boardColumnRequestMapper.toTaskListDto(boardColumn));

        return taskDto.build();
    }
}
