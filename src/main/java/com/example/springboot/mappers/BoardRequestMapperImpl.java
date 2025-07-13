package com.example.springboot.mappers;

import com.example.springboot.models.dto.BoardDto;
import com.example.springboot.models.dto.UserDto;
import com.example.springboot.models.entities.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardRequestMapperImpl implements BoardRequestMapper {

    @Override
    public BoardDto toBoardDto(Board board) {
        if (board == null) return null;

        return BoardDto.builder().build();
    }
}
