package com.example.springboot.repositories;


import com.example.springboot.models.dto.ProjectDto;
import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Найти проекты, в которых есть пользователь с заданным userId
    @Query("SELECT p, t FROM Project p " +
            "JOIN UserProject up ON p.id = up.project.id " +
            "JOIN User u ON up.user.id = u.id " +
            "LEFT JOIN Task t ON p.id = t.project.id " +
            "WHERE u.id = :userId")
    List<Project> findProjectsByUserId(Long userId);

    // Найти все проекты пользователя с userId, которые имеют хотя бы одну задачу с не завершенным статусом и с количеством комментариев больше 5.
    @Query("select p from Project p " +
            "join UserProject up on p.id = up.project.id " +
            "join User u on up.user.id = u.id " +
            "join Task t on p.id = t.project.id " +
            "left join Comment c on t.id = c.task.id " +
            "where u.id = :userId " +
            "and t.status != 'COMPLETED'" +
            "group by p " +
            "having count(c) > 1"
            )
    List<Project> findNotCompletedProjects(Long userId);

    // Найти все проекты, в которых участвуют хотя бы два пользователя, и в которых задачи старше определенной даты (например, старше 3 месяцев).
    @Query("select p from Project p")
    List<Project> findAllProjectsUnder3Months();

    // Найти проекты, в которых задачи выполняются позднее, чем их изначальная дата завершения (например, если dueDate задачи больше, чем originalDueDate)

    // Найти проекты, созданные в последние 6 месяцев

    @Query("select p from Project p " +
            "where p.createdAt >= :sixMonthAgo")
    List<Project> findRecentProjects(LocalDate sixMonthAgo);

    List<Project> findByIsDeletedTrueAndIsDeletedAtBefore(LocalDateTime isDeletedAtBefore);

    @Query("select p from Project p where p.isDeleted is null or p.isDeleted = false")
    List<Project> findAllNotDeleted();

    @Query("select p from Project p where p.key = :projectKey")
    Optional<Project> findByProjectKey(String projectKey);
}
