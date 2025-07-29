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
    private ActivityLogRepository activityLogRepository;
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
    public BoardColumnDTO createBoardColumn(Long boardId, String name) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        BoardColumn newBoardColumn = BoardColumn.builder()
                .name(name)
                .board(board)
                .build();
        boardColumnRepository.save(newBoardColumn);

        return boardColumnRequestMapper.toBoardColumnDto(newBoardColumn);
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
        return boardColumnRequestMapper.toBoardColumnDto(editBoardColumn);
    }

    @Override
    public List<BoardColumnDTO> getBoardColumns(Long boardId) {
        return boardColumnRepository.findAllByBoard_Id(boardId).stream()
                .map(boardColumnRequestMapper::toBoardColumnDto)
                .toList();

    }

    @Override
    public void deleteBoardColumnById(Long boardColumnDeleteId, Long boardColumnAssignId) {
        BoardColumn boardColumnToDelete = boardColumnRepository.findById(boardColumnDeleteId).orElseThrow(() -> new EntityNotFoundException("Board Column with ID " + boardColumnDeleteId + " Not Found"));
        BoardColumn boardColumnToAssign = boardColumnRepository.findById(boardColumnAssignId).orElseThrow(() -> new EntityNotFoundException("Board Column with ID " + boardColumnAssignId + " Not Found"));

        List<Task> tasks = taskRepository.findAllByBoardColumn_Id(boardColumnToDelete.getId());

        tasks.forEach(task -> {
            task.setBoardColumn(boardColumnToAssign);
            taskRepository.save(task);
        });

        boardColumnRepository.deleteById(boardColumnDeleteId);
    }

}
