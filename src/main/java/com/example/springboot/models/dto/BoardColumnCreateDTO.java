package com.example.springboot.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumnCreateDTO {
    private Long id;
    private String name;
    private String description;
    private Integer position;
    private BoardDto boardDto;
}
