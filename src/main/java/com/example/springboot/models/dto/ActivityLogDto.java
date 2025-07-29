package com.example.springboot.models.dto;

import com.example.springboot.models.entities.EntityType;
import com.example.springboot.models.enums.ActivityType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDto {
    private Long id;
    private EntityType entityType;
    private Long entityId;
    private ActivityType activityType;
    private Long userId;
    private Date changedAt;
}
