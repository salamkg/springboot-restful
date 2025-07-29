package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardRequestMapper;
import com.example.springboot.mappers.BoardColumnRequestMapper;
import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.*;
import com.example.springboot.models.enums.TaskStatus;
import com.example.springboot.repositories.BoardRepository;
import com.example.springboot.repositories.ActivityLogRepository;
import com.example.springboot.repositories.BoardColumnRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.BoardService;
import com.example.springboot.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardColumnRepository boardColumnRepository;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private BoardColumnRequestMapper boardColumnRequestMapper;
    @Autowired
    private UserService userService;

    @Override
    public BoardDto createBoard(String username, Board board) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User with " + username +" not found"));

        return null;
    }

    @Override
    public BoardDto updateBoard(Long boardId, Board board) {
        Board editBoard = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        //Get Board users to set username
        List<User> users = getBoardUsers(editBoard.getMembers());
        editBoard.setMembers(users);

        if (board.getBoardColumns() != null) {
            editBoard.setBoardColumns(board.getBoardColumns());
        }
        boardRepository.save(editBoard);

        return boardRequestMapper.toBoardDto(board);
    }

    public List<User> getBoardUsers(List<User> boardUsers) {
        List<User> users = new ArrayList<>();
        for (User user: boardUsers) {
            Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
            if (optionalUser.isPresent()) {
                users.add(optionalUser.get());
            } else {
                throw new RuntimeException("User with username " + user.getUsername() + " not found");
            }
        }
        return users;
    }

    @Override
    public void deleteBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

    }

    @Override
    public List<BoardDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board: boards) {
            boardDtos.add(boardRequestMapper.toBoardDto(board));
        }

        return boardDtos;
    }

    @Override
    public BoardDto getBoardById(Long id) {
        return null;
    }

    //TODO delete or remove method
    @Override
    public BoardColumnDTO createTaskListOnBoard(Long boardId, BoardColumn boardColumn) {
        if (boardColumn.getPosition() == null) {
            boardColumn.setPosition(1);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        board.getBoardColumns().add(boardColumn);
        boardColumn.setBoard(board);
        boardColumnRepository.save(boardColumn);

        return boardColumnRequestMapper.toBoardColumnDto(boardColumn);
    }

    //TODO delete or remove method
    @Override
    public BoardColumnDTO updateTaskListOnBoard(BoardColumn boardColumn) {
        boardColumnRepository.save(boardColumn);
        return boardColumnRequestMapper.toBoardColumnDto(boardColumn);
    }

    //TODO delete or remove method
    @Override
    public void updateTaskListPosition(Long boardId, Long taskListId, Integer newPosition) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        BoardColumn boardColumnToUpdate = board.getBoardColumns().stream()
                .filter(taskList -> taskList.getId().equals(taskListId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("List not found"));

        // Получаем текущую позицию
        Integer currentPosition = boardColumnToUpdate.getPosition();
        if (!currentPosition.equals(newPosition)) {
            if (newPosition > currentPosition) {
                boardColumnToUpdate.setPosition(newPosition);
            } else {
                boardColumnToUpdate.setPosition(currentPosition);
            }
        }

        boardColumnToUpdate.setPosition(newPosition);
        setTaskListNameByPosition(newPosition, boardColumnToUpdate);

        boardColumnRepository.save(boardColumnToUpdate);
    }

    //TODO delete or remove method
    public void setTaskListNameByPosition(Integer position, BoardColumn boardColumn) {
        switch (position) {
            case 1:
                boardColumn.setName(String.valueOf(TaskStatus.NEW));
                break;
            case 2:
                boardColumn.setName(String.valueOf(TaskStatus.PENDING));
                break;
            case 3:
                boardColumn.setName(String.valueOf(TaskStatus.TESTING));
                break;
            case 4:
                boardColumn.setName(String.valueOf(TaskStatus.COMPLETED));
                break;
            default:
                boardColumn.setName(boardColumn.getName());
        }
    }
}
