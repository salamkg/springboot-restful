package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardRequestMapper;
import com.example.springboot.mappers.TaskListRequestMapper;
import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.models.entities.TaskStatus;
import com.example.springboot.repositories.BoardRepository;
import com.example.springboot.repositories.TaskListRepository;
import com.example.springboot.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private TaskListRequestMapper taskListRequestMapper;

    @Override
    public BoardDto createBoard(Board board) {
        boardRepository.save(board);
        return boardRequestMapper.toBoardDto(board);
    }

    @Override
    public BoardDto updateBoard(Board board) {
        boardRepository.save(board);
        return boardRequestMapper.toBoardDto(board);
    }

    @Override
    public void deleteBoardById(Long id) {

    }

    @Override
    public List<BoardDto> getAllBoards() {
        return List.of();
    }

    @Override
    public BoardDto getBoardById(Long id) {
        return null;
    }

    @Override
    public TaskListDto createTaskListOnBoard(Long boardId, TaskList taskList) {
        if (taskList.getPosition() == null) {
            taskList.setPosition(1);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board does not exist"));

        board.getTaskLists().add(taskList);
        taskList.setBoard(board);
        taskListRepository.save(taskList);

        return taskListRequestMapper.toTaskListDto(taskList);
    }

    @Override
    public TaskListDto updateTaskListOnBoard(TaskList taskList) {
        taskListRepository.save(taskList);
        return taskListRequestMapper.toTaskListDto(taskList);
    }

    @Override
    public void updateTaskListPosition(Long boardId, Long taskListId, Integer newPosition) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board does not exist"));

        TaskList taskListToUpdate = board.getTaskLists().stream()
                .filter(taskList -> taskList.getId().equals(taskListId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("List not found"));

        // Получаем текущую позицию
        Integer currentPosition = taskListToUpdate.getPosition();
        if (!currentPosition.equals(newPosition)) {
            if (newPosition > currentPosition) {
                taskListToUpdate.setPosition(newPosition);
            } else {
                taskListToUpdate.setPosition(currentPosition);
            }
        }

        taskListToUpdate.setPosition(newPosition);
        setTaskListNameByPosition(newPosition, taskListToUpdate);

        taskListRepository.save(taskListToUpdate);
    }

    public void setTaskListNameByPosition(Integer position, TaskList taskList) {
        switch (position) {
            case 1:
                taskList.setName(TaskStatus.NEW);
                break;
            case 2:
                taskList.setName(TaskStatus.PENDING);
                break;
            case 3:
                taskList.setName(TaskStatus.COMPLETED);
                break;
            default:
                taskList.setName(taskList.getName());
        }
    }
}
