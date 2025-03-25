package com.example.springboot.mappers;

import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskListRequestMapperImpl implements TaskListRequestMapper {

    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public TaskListDto toTaskListDto(TaskList taskList) {
        if (taskList == null) {
            return null;
        }

        Board board = boardRepository.findById(taskList.getBoard().getId()).orElseThrow(() -> new RuntimeException("Board not found"));

        TaskListDto.TaskListDtoBuilder taskListDto = TaskListDto.builder();
        taskListDto.id(taskList.getId());
        taskListDto.name(taskList.getName());
        taskListDto.description(taskList.getDescription());
        taskListDto.position(taskList.getPosition());
        taskListDto.boardDto(boardRequestMapper.toBoardDto(board));

        return taskListDto.build();
    }
}
