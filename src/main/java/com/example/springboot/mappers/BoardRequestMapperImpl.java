package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.entities.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardRequestMapperImpl implements BoardRequestMapper {
    @Override
    public BoardDto toBoardDto(Board board) {
        if (board == null) {
            return null;
        }

        BoardDto.BoardDtoBuilder boardDto = BoardDto.builder();
        boardDto.id(board.getId());
        boardDto.name(board.getName());
        boardDto.description(board.getDescription());

        return boardDto.build();
    }
}
