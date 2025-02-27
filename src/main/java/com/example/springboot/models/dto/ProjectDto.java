package com.example.springboot.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;
    private String title;
    private String description;
    private TaskDto[] tasks;

//    private boolean includeTasks;
//
//    public TaskDto[] getTasks() {
//        if (includeTasks) {
//            return tasks;
//        }
//        return null;
//    }
//
//    public void setIncludeTasks(boolean includeTasks) {
//        this.includeTasks = includeTasks;
//    }

}
