package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_columns")
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer position;

    @ManyToOne()
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

}
