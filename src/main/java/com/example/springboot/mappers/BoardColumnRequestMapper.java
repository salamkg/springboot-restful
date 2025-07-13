package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardColumnDTO;
import com.example.springboot.models.entities.BoardColumn;

public interface BoardColumnRequestMapper {
    BoardColumnDTO toTaskListDto(BoardColumn boardColumn);
}
