package com.example.springboot.models.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLogDto {
    private Long id;
    private String entityType;
    private Long entityId;
    private String action;
    private String changedBy;
    private String changes;
    private Date changedAt;
}
