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
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    private String name;
//    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardColumn> boardColumns = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "board_members",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "project_id")
    private Project project;
}
