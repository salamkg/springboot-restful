package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.services.BoardColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@Tag(name = "Доска")
public class BoardColumnController {

    @Autowired
    private BoardColumnService boardColumnService;

//    @Autowired
    public BoardColumnController(BoardColumnService boardColumnService) {
        this.boardColumnService = boardColumnService;
    }

    @Operation(summary = "Доска колонок")
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<BoardColumnDTO>> getBoardColumns(@PathVariable Long boardId) {
        List<BoardColumnDTO> taskListDtoList = boardColumnService.getBoardColumns(boardId);
        return ResponseEntity.ok(taskListDtoList);
    }

    @PostMapping("/{boardId}/list/create")
    public ResponseEntity<BoardColumnDTO> createTaskList(@PathVariable Long boardId,
                                                         @RequestBody BoardColumn boardColumn) {

        BoardColumnDTO newTaskList = boardColumnService.createTaskList(boardId, boardColumn);
        return ResponseEntity.ok(newTaskList);
    }

    @PatchMapping("/{boardId}/lists/{taskListId}/edit")
    public ResponseEntity<BoardColumnDTO> editTaskList(@PathVariable Long boardId,
                                                       @PathVariable Long taskListId,
                                                       @RequestBody BoardColumn boardColumn) {

        BoardColumnDTO newTaskList = boardColumnService.updateTaskList(boardId, taskListId, boardColumn);
        return ResponseEntity.ok(newTaskList);
    }

    @Operation(summary = "Удаление колонки")
    @DeleteMapping("/columns/{id}/remove")
    public void deleteTaskList(@PathVariable Long id, @RequestParam String name) {
        boardColumnService.deleteBoardColumnById(id, name);
    }
}
