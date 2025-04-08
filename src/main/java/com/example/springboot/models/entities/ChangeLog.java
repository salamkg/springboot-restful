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

    private String entityType;
    private Long entityId;
    private String action; // Тип изменения
    private String changedBy;

    @Column(name = "attribute_names", columnDefinition = "TEXT")
    private String attributeNames;

    @Column(columnDefinition = "TEXT")
    private String changes;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private Date changedAt;

}
