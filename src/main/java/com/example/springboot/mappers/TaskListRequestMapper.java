package com.example.springboot.mappers;

import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.TaskList;

public interface TaskListRequestMapper {
    TaskListDto toTaskListDto(TaskList taskList);
}
