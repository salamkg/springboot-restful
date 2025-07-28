package com.example.springboot.services;

import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.BoardColumn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardColumnService {

    BoardColumnDTO createBoardColumn(Long boardId, String name);

    void deleteBoardColumnById(Long deleteId, Long newId);

    BoardColumnDTO updateTaskList(Long boardId, Long taskListId, BoardColumn boardColumn);

    List<BoardColumnDTO> getBoardColumns(Long boardId);
}
