package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.repositories.TaskListRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class TaskRequestMapperImpl implements TaskRequestMapper {
    private final TaskListRepository taskListRepository;
    private final TaskListRequestMapper taskListRequestMapper;

    public TaskRequestMapperImpl(TaskListRepository taskListRepository, TaskListRequestMapper taskListRequestMapper) {
        this.taskListRepository = taskListRepository;
        this.taskListRequestMapper = taskListRequestMapper;
    }

    @Override
    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskList taskList = taskListRepository.findById(task.getTaskList().getId()).orElseThrow(() -> new RuntimeException("TaskList Not Found"));

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
        taskDto.taskListDto(taskListRequestMapper.toTaskListDto(taskList));

        return taskDto.build();
    }
}
