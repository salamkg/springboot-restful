package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDependency {

    //TaskDependency — сущность для описания зависимостей между задачами.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Task task; // задача от которой зависит другая задача

    @ManyToOne
    private Task dependency; // задача, от которой зависит текущая
}
