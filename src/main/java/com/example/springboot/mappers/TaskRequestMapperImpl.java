package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskRequestMapperImpl implements TaskRequestMapper {
    @Override
    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskDto.TaskDtoBuilder taskDto = TaskDto.builder();
        taskDto.id(task.getId());
        taskDto.title(task.getName());
        taskDto.description(task.getDescription());
        taskDto.comments(task.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getText()
                )).toArray(CommentDto[]::new));

        return taskDto.build();
    }
}
