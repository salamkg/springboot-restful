package com.example.springboot.controllers;

import com.example.springboot.audit.ActivityLog;
import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.models.entities.EntityType;
import com.example.springboot.models.enums.ActivityType;
import com.example.springboot.services.BoardColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@Tag(name = "Доска колонок")
public class BoardColumnController {

    @Autowired
    private BoardColumnService boardColumnService;

    @Operation(summary = "Просмотр колонок доски")
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<BoardColumnDTO>> getBoardColumns(@PathVariable Long boardId) {
        List<BoardColumnDTO> taskListDtoList = boardColumnService.getBoardColumns(boardId);
        return ResponseEntity.ok(taskListDtoList);
    }

    @Operation(summary = "Создать колонку")
    @PostMapping("/{boardId}/createColumn")
    public ResponseEntity<BoardColumnDTO> createBoardColumn(@PathVariable Long boardId,
                                                            @RequestParam String name) {

        BoardColumnDTO newTaskList = boardColumnService.createBoardColumn(boardId, name);
        return ResponseEntity.ok(newTaskList);
    }

    @PatchMapping("/{boardId}/lists/{taskListId}/edit")
    public ResponseEntity<BoardColumnDTO> editTaskList(@PathVariable Long boardId,
                                                       @PathVariable Long taskListId,
                                                       @RequestBody BoardColumn boardColumn) {

        BoardColumnDTO newTaskList = boardColumnService.updateTaskList(boardId, taskListId, boardColumn);
        return ResponseEntity.ok(newTaskList);
    }

    @ActivityLog(type = ActivityType.DELETE, entity = EntityType.BOARD_COLUMN)
    @Operation(summary = "Удаление колонки")
    @DeleteMapping("/columns/{id}/remove")
    public void deleteTaskList(@PathVariable(name = "id") Long deleteId, @RequestParam Long newId) {
        boardColumnService.deleteBoardColumnById(deleteId, newId);
    }
}
