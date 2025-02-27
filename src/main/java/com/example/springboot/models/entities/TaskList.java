package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_list")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ColumnDefault("NEW")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus name = TaskStatus.NEW;

    private String description;

    private Integer position;

    @ManyToOne()
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

}
