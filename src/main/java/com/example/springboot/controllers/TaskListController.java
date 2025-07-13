package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.services.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

//    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping("/{boardId}/lists")
    public ResponseEntity<List<BoardColumnDTO>> getTaskLists(@PathVariable Long boardId) {
        List<BoardColumnDTO> taskListDtoList = taskListService.getAllTaskLists(boardId);
        return ResponseEntity.ok(taskListDtoList);
    }

    @PostMapping("/{boardId}/list/create")
    public ResponseEntity<BoardColumnDTO> createTaskList(@PathVariable Long boardId,
                                                         @RequestBody BoardColumn boardColumn) {

        BoardColumnDTO newTaskList = taskListService.createTaskList(boardId, boardColumn);
        return ResponseEntity.ok(newTaskList);
    }

    @PatchMapping("/{boardId}/lists/{taskListId}/edit")
    public ResponseEntity<BoardColumnDTO> editTaskList(@PathVariable Long boardId,
                                                       @PathVariable Long taskListId,
                                                       @RequestBody BoardColumn boardColumn) {

        BoardColumnDTO newTaskList = taskListService.updateTaskList(boardId, taskListId, boardColumn);
        return ResponseEntity.ok(newTaskList);
    }

    @DeleteMapping("/lists/{taskListId}/remove")
    public void deleteTaskList(@PathVariable Long taskListId) {
        taskListService.deleteTaskListById(taskListId);
    }
}
