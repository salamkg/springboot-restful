package com.example.springboot.services;

import com.example.springboot.models.entities.ActivityLog;
import com.example.springboot.models.entities.EntityType;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.enums.ActivityType;
import com.example.springboot.repositories.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public void logActivity(Long userId, Long entityId,
                            EntityType entityType, ActivityType activityType) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setEntityId(entityId);
        log.setEntityType(entityType);
        log.setActivityType(activityType);
        log.setChangedAt(new Date());
        activityLogRepository.save(log);
    }
}
