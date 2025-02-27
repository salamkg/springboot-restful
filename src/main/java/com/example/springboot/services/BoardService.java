package com.example.springboot.services;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.TaskList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {

    BoardDto createBoard(Board board);
    BoardDto updateBoard(Board board);
    TaskListDto createTaskListOnBoard(Long boardId, TaskList taskList);
    TaskListDto updateTaskListOnBoard(TaskList taskList);
    void updateTaskListPosition(Long boardId, Long taskListId, Integer position);

    void deleteBoardById(Long id);
    List<BoardDto> getAllBoards();
    BoardDto getBoardById(Long id);
}
