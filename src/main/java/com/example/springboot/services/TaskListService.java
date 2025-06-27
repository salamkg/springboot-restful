package com.example.springboot.services;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.TaskList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskListService {

    TaskListDto createTaskList(Long boardId, TaskList taskList);

    void deleteTaskListById(Long taskListId);

    TaskListDto updateTaskList(Long boardId, Long taskListId, TaskList taskList);

    List<TaskListDto> getAllTaskLists(Long boardId);
}
