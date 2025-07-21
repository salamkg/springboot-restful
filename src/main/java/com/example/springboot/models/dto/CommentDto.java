package com.example.springboot.models.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String text;
    private Long parentId;
    private List<CommentDto> replies = new ArrayList<>();
}
