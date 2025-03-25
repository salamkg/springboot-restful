package com.example.springboot.services.impl;

import com.example.springboot.mappers.BoardRequestMapper;
import com.example.springboot.mappers.TaskListRequestMapper;
import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.*;
import com.example.springboot.repositories.BoardRepository;
import com.example.springboot.repositories.ChangeLogRepository;
import com.example.springboot.repositories.TaskListRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.security.CustomUserDetails;
import com.example.springboot.services.BoardService;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;
    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private TaskListRequestMapper taskListRequestMapper;
    @Autowired
    private UserService userService;

    @Override
    public BoardDto createBoard(String username, Board board) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Board newBoard = new Board();
        newBoard.setId(board.getId());
        newBoard.setName(board.getName());
        newBoard.setDescription(board.getDescription());
        newBoard.getUsers().add(user);
        boardRepository.save(newBoard);

        //Logging
        ChangeLog changeLog = new ChangeLog();
        changeLog.setBoardId(newBoard.getId());
        changeLog.setAction("create");
        changeLog.setChangedBy(user.getFirstName());
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        return boardRequestMapper.toBoardDto(newBoard);
    }

    @Override
    public BoardDto updateBoard(Long boardId, Board board) {
        Board editBoard = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board For Edit Not Found"));
        if (board.getName() != null) {
            editBoard.setName(board.getName());
        }
        if (board.getDescription() != null) {
            editBoard.setDescription(board.getDescription());
        }
        //Get Board users to set username
        List<User> users = getBoardUsers(editBoard.getUsers());
        editBoard.setUsers(users);

        if (board.getTaskLists() != null) {
            editBoard.setTaskLists(board.getTaskLists());
        }
        boardRepository.save(editBoard);

        //Logging
        ChangeLog changeLog = new ChangeLog();
        changeLog.setBoardId(board.getId());
        changeLog.setAction("edit");
        for (User user: users) {
            Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
            if (optionalUser.isPresent()) {
                changeLog.setChangedBy(optionalUser.get().getFirstName());
            } else {
                throw new RuntimeException("User with username " + user.getUsername() + " not found");
            }
        }
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

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
                .orElseThrow(() -> new RuntimeException(String.format("Board with id %d does not exist", boardId)));

        ChangeLog changeLog = new ChangeLog();
        changeLog.setBoardId(boardId);
        changeLog.setAction("delete");

        //Get Board users to set username
        for (User user: getBoardUsers(board.getUsers())) {
            Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
            if (optionalUser.isPresent()) {
                changeLog.setChangedBy(optionalUser.get().getFirstName());
            } else {
                throw new RuntimeException("User with username " + user.getUsername() + " not found");
            }
        }
        boardRepository.deleteById(boardId);
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);
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

    //TODO delete or remove method
    @Override
    public TaskListDto updateTaskListOnBoard(TaskList taskList) {
        taskListRepository.save(taskList);
        return taskListRequestMapper.toTaskListDto(taskList);
    }

    //TODO delete or remove method
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

    //TODO delete or remove method
    public void setTaskListNameByPosition(Integer position, TaskList taskList) {
        switch (position) {
            case 1:
                taskList.setName(String.valueOf(TaskStatus.NEW));
                break;
            case 2:
                taskList.setName(String.valueOf(TaskStatus.PENDING));
                break;
            case 3:
                taskList.setName(String.valueOf(TaskStatus.COMPLETED));
                break;
            default:
                taskList.setName(taskList.getName());
        }
    }
}
