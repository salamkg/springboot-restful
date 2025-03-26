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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));

        TaskList newTaskList = new TaskList();
        newTaskList.setName(taskList.getName());
        newTaskList.setDescription(taskList.getDescription());
        newTaskList.setPosition(taskList.getPosition());
        newTaskList.setBoard(board);
        taskListRepository.save(newTaskList);

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(newTaskList.getId());
        changeLog.setEntity("TaskList");
        changeLog.setAction("create");
        changeLog.setChangedBy(firstName);
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        return taskListRequestMapper.toTaskListDto(newTaskList);
    }

    @Override
    public TaskListDto updateTaskList(Long boardId, Long taskListId, TaskList taskList) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found")); //TODO remove?
        TaskList editTaskList = taskListRepository.findById(taskListId).orElseThrow(() -> new RuntimeException("TaskList for edit not found"));
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

        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(editTaskList.getId());
        changeLog.setEntity("TaskList");
        changeLog.setAction("edit");
        changeLog.setChangedBy(firstName);
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        return taskListRequestMapper.toTaskListDto(editTaskList);
    }

    @Override
    public void deleteTaskListById(Long taskListId) {
        //Logging
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();

        TaskList taskList = taskListRepository.findById(taskListId).orElseThrow(() -> new RuntimeException("TaskList Not Found"));
        Board board = boardRepository.findById(taskList.getBoard().getId()).orElseThrow(() -> new RuntimeException("Board Not Found"));

        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(taskList.getId());
        changeLog.setEntity("TaskList");
        changeLog.setChangedBy(firstName);
        changeLog.setAction("delete");
        changeLog.setTimestamp(new Date());
        changeLogRepository.save(changeLog);

        taskListRepository.deleteById(taskListId);
    }

}
