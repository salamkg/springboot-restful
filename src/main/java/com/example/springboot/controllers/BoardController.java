package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Api(tags = {Swagger2Config.Board})
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // Get Boards
    @GetMapping()
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        List<BoardDto> boardDtos = boardService.getAllBoards();

        return ResponseEntity.ok(boardDtos);
    }

    // Create Board
    @PostMapping()
    public ResponseEntity<BoardDto> createBoard(@RequestParam String username, @RequestBody Board board) {
        BoardDto boardDto = boardService.createBoard(username, board);

        return new ResponseEntity<>(boardDto, HttpStatus.CREATED);
    }

    // Edit Board
    @PutMapping("/{boardId}/edit")
    public ResponseEntity<BoardDto> editBoard(@PathVariable Long boardId, @RequestBody Board board) {
        board.setId(boardId);
        BoardDto boardDto = boardService.updateBoard(boardId, board);

        return ResponseEntity.ok(boardDto);
    }

    // Create List on Board
    @PostMapping("/{boardId}/lists")
    public ResponseEntity<BoardColumnDTO> createTaskListOnBoard(@PathVariable Long boardId, @RequestBody BoardColumn boardColumn) {
        BoardColumnDTO newTaskList = boardService.createTaskListOnBoard(boardId, boardColumn);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskList);
    }

    // Edit TaskList on Board
    @PutMapping("/lists/{listId}")
    public ResponseEntity<BoardColumnDTO> editTaskList(@PathVariable Long listId, @RequestBody BoardColumn boardColumn) {
        boardColumn.setId(listId);
        BoardColumnDTO boardColumnDTO = boardService.updateTaskListOnBoard(boardColumn);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    // Edit TaskList on Board
    @PutMapping("/{boardId}/lists/{listId}/order")
    public ResponseEntity<BoardColumnDTO> updateTaskListPosition(@PathVariable Long boardId,
                                                                 @PathVariable Long listId,
                                                                 @RequestParam(name = "newPosition") Integer position) {
        boardService.updateTaskListPosition(boardId, listId, position);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{boardId}/remove")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoardById(boardId);
    }
}
