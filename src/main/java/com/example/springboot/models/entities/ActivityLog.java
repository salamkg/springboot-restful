package com.example.springboot.models.entities;

import com.example.springboot.models.enums.ActivityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_logs")
@Builder
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private Long entityId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

//    @Column(columnDefinition = "TEXT")
//    private String changes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private Date changedAt;

}
