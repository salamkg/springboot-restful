package com.example.springboot.services.impl;

import com.example.springboot.mappers.TaskListRequestMapper;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.ChangeLog;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.repositories.BoardRepository;
import com.example.springboot.repositories.ChangeLogRepository;
import com.example.springboot.repositories.TaskListRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.TaskListService;
import com.example.springboot.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskListServiceImpl implements TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskListRequestMapper taskListRequestMapper;
    @Autowired
    private UserService userService;

    @Override
    public TaskListDto createTaskList(Long boardId, TaskList taskList) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found"));

        TaskList newTaskList = new TaskList();
        newTaskList.setName(taskList.getName());
        newTaskList.setDescription(taskList.getDescription());
        newTaskList.setPosition(taskList.getPosition());
        newTaskList.setBoard(board);
        taskListRepository.save(newTaskList);

        return taskListRequestMapper.toTaskListDto(newTaskList);
    }

    @Override
    public TaskListDto updateTaskList(Long boardId, Long taskListId, TaskList taskList) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + boardId + " Not Found")); //TODO remove?
        TaskList editTaskList = taskListRepository.findById(taskListId).orElseThrow(() -> new EntityNotFoundException("TaskList with ID " + taskListId + " Not Found"));
        if (taskList.getName() != null) {
            editTaskList.setName(taskList.getName());
        }
        if (taskList.getDescription() != null) {
            editTaskList.setDescription(taskList.getDescription());
        }
        if (taskList.getPosition() != null) {
            editTaskList.setPosition(taskList.getPosition());
        }
        if (taskList.getTasks() != null) {
            editTaskList.setTasks(taskList.getTasks());
        }
        taskListRepository.save(editTaskList);
        return taskListRequestMapper.toTaskListDto(editTaskList);
    }

    @Override
    public List<TaskListDto> getAllTaskLists(Long boardId) {
        return taskListRepository.findAllByBoard_Id(boardId).stream()
                .map(taskListRequestMapper::toTaskListDto)
                .toList();

    }

    @Override
    public void deleteTaskListById(Long taskListId) {
        //Logging
        String firstName;
//        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        taskListRepository.findById(taskListId).orElseThrow(() -> new EntityNotFoundException("Board with ID " + taskListId + " Not Found"));
//        Board board = boardRepository.findById(taskList.getBoard().getId()).orElseThrow(() -> new EntityNotFoundException("Board with ID " + taskList.getBoard().getId() + " Not Found"));

        taskListRepository.deleteById(taskListId);
    }

}
