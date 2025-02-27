package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.TaskListDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.TaskList;
import com.example.springboot.services.BoardService;
import com.example.springboot.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Api(tags = {Swagger2Config.Board})
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // Create Board
    @PostMapping()
    public ResponseEntity<BoardDto> createBoard(@RequestBody Board board) {
        BoardDto boardDto = boardService.createBoard(board);

        return new ResponseEntity<>(boardDto, HttpStatus.CREATED);
    }

    // Edit Board
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto> editBoard(@PathVariable Long boardId, @RequestBody Board board) {
        board.setId(boardId);
        BoardDto boardDto = boardService.updateBoard(board);

        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    // Create List on Board
    @PostMapping("/{boardId}/lists")
    public ResponseEntity<TaskListDto> createTaskListOnBoard(@PathVariable Long boardId, @RequestBody TaskList taskList) {
        TaskListDto newTaskList = boardService.createTaskListOnBoard(boardId, taskList);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskList);
    }

    // Edit TaskList on Board
    @PutMapping("/lists/{listId}")
    public ResponseEntity<TaskListDto> editTaskList(@PathVariable Long listId, @RequestBody TaskList taskList) {
        taskList.setId(listId);
        TaskListDto taskListDto = boardService.updateTaskListOnBoard(taskList);

        return new ResponseEntity<>(taskListDto, HttpStatus.OK);
    }

    // Edit TaskList on Board
    @PutMapping("/{boardId}/lists/{listId}/order")
    public ResponseEntity<TaskListDto> updateTaskListPosition(@PathVariable Long boardId,
                                                              @PathVariable Long listId,
                                                              @RequestParam(name = "newPosition") Integer position) {
        boardService.updateTaskListPosition(boardId, listId, position);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
