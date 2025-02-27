package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.TaskList;
import org.springframework.stereotype.Component;

@Component
public class TaskListRequestMapperImpl implements TaskListRequestMapper {

    @Override
    public TaskListDto toTaskListDto(TaskList taskList) {
        if (taskList == null) {
            return null;
        }

        TaskListDto.TaskListDtoBuilder taskListDto = TaskListDto.builder();
        taskListDto.id(taskList.getId());
        taskListDto.name(taskList.getName());
        taskListDto.description(taskList.getDescription());
        taskListDto.position(taskList.getPosition());

        return taskListDto.build();
    }
}
