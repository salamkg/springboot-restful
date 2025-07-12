package com.example.springboot.models.entities;

import com.example.springboot.models.enums.ProjectType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ProjectType type; //KANBAN, AGILE

    @Column(unique = true)
    private String key;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    // TODO Добавление пользователей в проект
    @OneToMany(mappedBy = "project")
    private List<User> people = new ArrayList<>();

    // TODO менеджер проекта
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User lead;

    // TODO при удалении проект = TRUE и удаляется безвозвратно через x дней
    private Boolean isDeleted = Boolean.FALSE;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime isDeletedAt;

    private String projectUrl;
    private String avatar;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date updatedAt;

}
