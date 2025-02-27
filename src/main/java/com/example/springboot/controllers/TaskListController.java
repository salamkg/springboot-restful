package com.example.springboot.controllers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.entities.Board;
import com.example.springboot.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards/list")
public class TaskListController {

//    private final BoardService boardService;
//
//    @Autowired
//    public TaskListController(BoardService boardService) {
//        this.boardService = boardService;
//    }

//    @PostMapping()
//    public ResponseEntity<BoardDto> createBoard(@RequestBody Board board) {
//        BoardDto boardDto = boardService.createBoard(board);
//
//        return new ResponseEntity<>(boardDto, HttpStatus.CREATED);
//    }
}
