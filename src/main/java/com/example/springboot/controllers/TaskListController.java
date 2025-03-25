package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.services.BoardService;
import com.example.springboot.services.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class TaskListController {

    private TaskListService taskListService;

//    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @PostMapping("/{boardId}/list/create")
    public ResponseEntity<TaskListDto> createTaskList(@PathVariable Long boardId,
                                                      @RequestBody TaskList taskList) {

        TaskListDto newTaskList = taskListService.createTaskList(boardId, taskList);
        return ResponseEntity.ok(newTaskList);
    }

    @PutMapping("/{boardId}/list/{taskListId}/edit")
    public ResponseEntity<TaskListDto> editTaskList(@PathVariable Long boardId,
                                                    @PathVariable Long taskListId,
                                                    @RequestBody TaskList taskList) {

        TaskListDto newTaskList = taskListService.updateTaskList(boardId, taskListId, taskList);
        return ResponseEntity.ok(newTaskList);
    }

    @DeleteMapping("/lists/{taskListId}/remove")
    public void deleteTaskList(@PathVariable Long taskListId) {
        taskListService.deleteTaskListById(taskListId);
    }
}
