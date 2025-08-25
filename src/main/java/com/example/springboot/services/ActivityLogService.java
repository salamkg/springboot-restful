package com.example.springboot.services;

import com.example.springboot.models.dto.ActivityLogDto;
import com.example.springboot.models.enums.EntityType;
import com.example.springboot.models.enums.ActivityType;

import java.util.List;


public interface ActivityLogService {
    void logActivity(Long userId, Long entityId, EntityType entityType, ActivityType activityType);
    List<ActivityLogDto> getHistory(Long userId);
}
