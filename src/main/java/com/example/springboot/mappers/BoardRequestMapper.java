package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.entities.Board;

public interface BoardRequestMapper {

    BoardDto toBoardDto(Board board);
}
