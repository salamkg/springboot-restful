package com.example.springboot.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "changelog")
@Builder
public class ChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String entityType;
    private Long entityId;
    private String action; // Тип изменения
    private String changedBy;

    @Column(columnDefinition = "TEXT")
    private String changes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private Date changedAt;

}
