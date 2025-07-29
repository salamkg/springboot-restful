package com.example.springboot.mappers;

import com.example.springboot.models.dto.ActivityLogDto;
import com.example.springboot.models.entities.ActivityLog;
import org.springframework.stereotype.Component;

@Component
public class ActivityLogMapperImpl implements ActivityLogMapper {
    @Override
    public ActivityLogDto toChangeLogDto(ActivityLog activityLog) {
        return ActivityLogDto.builder()
                .id(activityLog.getId())
                .entityId(activityLog.getEntityId())
                .entityType(activityLog.getEntityType())
                .activityType(activityLog.getActivityType())
                .userId(activityLog.getUserId())
                .changedAt(activityLog.getChangedAt())
                .build();
    }
}
