package com.example.springboot.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;

    @Column(unique = true)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 3, message = "Password should have min 3 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany
    @JoinTable(
            name = "user_project_role", // промежуточная таблица
            joinColumns = @JoinColumn(name = "user_id"), // Внешний ключ для пользователя
            inverseJoinColumns = @JoinColumn(name = "role_id")) // Внешний ключ для роли
    private Set<Role> roles;

    @ManyToMany
//    @JsonBackReference
    @JoinTable(
            name = "user_project", // промежуточная таблица
            joinColumns = @JoinColumn(name = "user_id"), // Внешний ключ для пользователя
            inverseJoinColumns = @JoinColumn(name = "project_id")) // Внешний ключ для проекта
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "assignedUsers")
    private List<Task> assignedTasks = new ArrayList<>();
}
