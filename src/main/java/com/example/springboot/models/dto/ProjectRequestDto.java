package com.example.springboot.models.dto;

import com.example.springboot.models.enums.ProjectType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

    private String name;
    private String key;
    private ProjectType type;
    private Boolean isAvailable = true;
//    private List<BoardDto> boards;
//    private List<UserDto> people;
    private UserDto lead;
    private Boolean isDeleted;
}
