package com.example.springboot.mappers;

import com.example.springboot.models.dto.ChangeLogDto;
import com.example.springboot.models.entities.ChangeLog;

public interface ChangeLogMapper {

    ChangeLogDto toChangeLogDto(ChangeLog changeLog);
}
