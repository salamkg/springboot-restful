package com.example.springboot.models.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private Long id;
    private String name;
    private String key;
    private String type;
    private List<BoardDto> boards;
    private List<UserDto> people;
    private UserDto lead;
}
