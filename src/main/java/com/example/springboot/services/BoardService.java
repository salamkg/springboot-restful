package com.example.springboot.services;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.BoardColumn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {

    BoardDto createBoard(String username, Board board);
    BoardDto updateBoard(Long boardId, Board board);
    BoardColumnDTO createTaskListOnBoard(Long boardId, BoardColumn boardColumn);
    BoardColumnDTO updateTaskListOnBoard(BoardColumn boardColumn);
    void updateTaskListPosition(Long boardId, Long taskListId, Integer position);

    void deleteBoardById(Long id);
    List<BoardDto> getAllBoards();
    BoardDto getBoardById(Long id);
}
