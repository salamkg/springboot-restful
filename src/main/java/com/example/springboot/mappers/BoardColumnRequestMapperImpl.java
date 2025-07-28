package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardColumnRequestMapperImpl implements BoardColumnRequestMapper {

    @Autowired
    private BoardRequestMapper boardRequestMapper;
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public BoardColumnDTO toBoardColumnDto(BoardColumn boardColumn) {
        if (boardColumn == null) {
            return null;
        }

        Board board = boardRepository.findById(boardColumn.getBoard().getId()).orElseThrow(() -> new RuntimeException("Board not found"));

        BoardColumnDTO boardColumnDTO = BoardColumnDTO.builder()
                .id(boardColumn.getId())
                .name(boardColumn.getName())
                .boardDto(boardRequestMapper.toBoardDto(board))
                .build();

        return boardColumnDTO;
    }
}
