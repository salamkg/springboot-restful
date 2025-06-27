package com.example.springboot.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type; //KANBAN, AGILE
    private String key;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    // assignees
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<User> people = new ArrayList<>();

    //менеджер проекта
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User lead;

    private String projectUrl;
    private String avatar;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate updatedAt;

}
