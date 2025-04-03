package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@Builder
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
    private TaskStatus status;

    private Integer position;

    private String priority;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate; // Дата завершения задачи

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "task_users", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> assignedUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(mappedBy = "tasks")
    private List<Tag> tags = new ArrayList<>();

    // Здесь задача может зависеть от другой задачи с помощью @ManyToMany в сущности Task, а также хранить подзадачи через @OneToMany
    // Подзадачи можно реализовать, используя рекурсивные связи в Task, где одна задача может быть подзадачей другой задачи.
    // В таком случае задачи могут быть связаны через поле parentTask. Если задача не имеет родительской задачи, то она будет основной задачей.
    // Для учета времени, когда задача должна начинаться и заканчиваться, используйте поля startDate и endDate в сущности Task.
    // startDate — дата начала выполнения задачи.
    // endDate — дата окончания задачи

    @OneToMany(mappedBy = "parentTask")
    private List<Task> subTasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @ManyToMany
    @JoinTable(
            name = "task_dependencies",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "dependency_id")
    )
    private List<Task> dependencies = new ArrayList<>();
}
