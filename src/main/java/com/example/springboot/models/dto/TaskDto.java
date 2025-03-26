package com.example.springboot.models.dto;

import com.example.springboot.models.entities.TaskStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private String priority;
    private CommentDto[] comments;
    private TaskListDto taskListDto;
    private MultipartFile files;
}
