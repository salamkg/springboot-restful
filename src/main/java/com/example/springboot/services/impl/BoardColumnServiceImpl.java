package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardColumnRequestMapper;
import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.models.entities.Task;
import com.example.springboot.repositories.*;
import com.example.springboot.services.BoardColumnService;
import com.example.springboot.services.TaskService;
import com.example.springboot.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardColumnServiceImpl implements BoardColumnService {

    @Autowired
    private BoardColumnRepository boardColumnRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardColumnRequestMapper boardColumnRequestMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;

    @Override
    public BoardColumnDTO createTaskList(Long boardId, BoardColumn boardColumn) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        BoardColumn newBoardColumn = new BoardColumn();
        newBoardColumn.setName(boardColumn.getName());
        newBoardColumn.setDescription(boardColumn.getDescription());
        newBoardColumn.setPosition(boardColumn.getPosition());
        newBoardColumn.setBoard(board);
        boardColumnRepository.save(newBoardColumn);

        return boardColumnRequestMapper.toTaskListDto(newBoardColumn);
    }

    @Override
    public BoardColumnDTO updateTaskList(Long boardId, Long taskListId, BoardColumn boardColumn) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found")); //TODO remove?
        BoardColumn editBoardColumn = boardColumnRepository.findById(taskListId).orElseThrow(() -> new EntityNotFoundException("TaskList with ID " + taskListId + " Not Found"));
        if (boardColumn.getName() != null) {
            editBoardColumn.setName(boardColumn.getName());
        }
        if (boardColumn.getDescription() != null) {
            editBoardColumn.setDescription(boardColumn.getDescription());
        }
        if (boardColumn.getPosition() != null) {
            editBoardColumn.setPosition(boardColumn.getPosition());
        }
        if (boardColumn.getTasks() != null) {
            editBoardColumn.setTasks(boardColumn.getTasks());
        }
        boardColumnRepository.save(editBoardColumn);
        return boardColumnRequestMapper.toTaskListDto(editBoardColumn);
    }

    @Override
    public List<BoardColumnDTO> getBoardColumns(Long boardId) {
        return boardColumnRepository.findAllByBoard_Id(boardId).stream()
                .map(boardColumnRequestMapper::toTaskListDto)
                .toList();

    }

    @Override
    public void deleteBoardColumnById(Long boardColumnId, String name) {

        BoardColumn boardColumnToDelete = boardColumnRepository.findById(boardColumnId).orElseThrow(() -> new EntityNotFoundException("Board Column with ID " + boardColumnId + " Not Found"));
        BoardColumn boardColumnNew = boardColumnRepository.findBoardColumnByNameContainingIgnoreCase(name);
        List<Task> tasks = taskRepository.findAllByBoardColumn_Id(boardColumnToDelete.getId());

        tasks.forEach(task -> {
            task.setBoardColumn(boardColumnNew);
            taskRepository.save(task);
        });

        boardColumnRepository.deleteById(boardColumnId);
    }

}
