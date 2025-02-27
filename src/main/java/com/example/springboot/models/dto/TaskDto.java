package com.example.springboot.models.dto;

import com.example.springboot.models.entities.TaskStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private CommentDto[] comments;
}
