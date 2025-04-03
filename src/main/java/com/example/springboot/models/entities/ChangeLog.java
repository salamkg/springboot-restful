package com.example.springboot.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String entity;
    private Long entityId;
    private String action; // Тип изменения
    private String changedBy;

    @Column(name = "name")
    private String entityName;

    @Column(name = "description")
    private String entityDescription;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @Column(name = "position")
    private Integer entityPosition;

    private String priority;

    @Lob
    @Column(name = "data", columnDefinition = "TEXT")
    private String changelogDataJson;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated_at;

}
