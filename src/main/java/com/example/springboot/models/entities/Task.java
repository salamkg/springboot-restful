package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachedFiles;

    @ColumnDefault("NEW")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status = TaskStatus.NEW;

    private Integer position;

    private String priority;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate; // Дата завершения задачи

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task")
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    @ManyToMany
    @JoinTable(name = "task_users", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> assignedUsers = new ArrayList<>();
}
