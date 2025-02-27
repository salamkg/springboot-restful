package com.example.springboot.models.dto;

import com.example.springboot.models.entities.TaskStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskListDto {
    private Long id;
    private TaskStatus name;
    private String description;
    private Integer position;
}
