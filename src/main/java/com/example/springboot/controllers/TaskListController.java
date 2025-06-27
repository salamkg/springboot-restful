package com.example.springboot.controllers;

import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.TaskList;
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
    public ResponseEntity<List<TaskListDto>> getTaskLists(@PathVariable Long boardId) {
        List<TaskListDto> taskListDtoList = taskListService.getAllTaskLists(boardId);
        return ResponseEntity.ok(taskListDtoList);
    }

    @PostMapping("/{boardId}/list/create")
    public ResponseEntity<TaskListDto> createTaskList(@PathVariable Long boardId,
                                                      @RequestBody TaskList taskList) {

        TaskListDto newTaskList = taskListService.createTaskList(boardId, taskList);
        return ResponseEntity.ok(newTaskList);
    }

    @PatchMapping("/{boardId}/lists/{taskListId}/edit")
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
