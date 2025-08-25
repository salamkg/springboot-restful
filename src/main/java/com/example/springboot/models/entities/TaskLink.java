package com.example.springboot.models.entities;

import com.example.springboot.models.enums.TaskLinkType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TaskLinkType linkType;

    private String targetLink;

    private String sourceLink;
}
