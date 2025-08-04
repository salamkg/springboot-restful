package com.example.springboot.mappers;

import com.example.springboot.models.dto.ActivityLogDto;
import com.example.springboot.models.entities.ActivityLog;

public interface ActivityLogMapper {

    ActivityLogDto toDTO(ActivityLog activityLog);
}
