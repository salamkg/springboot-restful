package com.example.springboot.mappers;

import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;

public interface TaskRequestMapper {

    TaskDto toTaskDto(Task task);
}
