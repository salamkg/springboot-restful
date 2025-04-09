package com.example.springboot.mappers;

import com.example.springboot.models.dto.ChangeLogDto;
import com.example.springboot.models.entities.ChangeLog;
import org.springframework.stereotype.Component;

@Component
public class ChangeLogMapperImpl implements ChangeLogMapper {
    @Override
    public ChangeLogDto toChangeLogDto(ChangeLog changeLog) {
        return ChangeLogDto.builder()
                .id(changeLog.getId())
                .entityId(changeLog.getEntityId())
                .entityType(changeLog.getEntityType())
                .action(changeLog.getAction())
                .changedBy(changeLog.getChangedBy())
                .changedAt(changeLog.getChangedAt())
                .changes(changeLog.getChanges())
                .build();
    }
}
