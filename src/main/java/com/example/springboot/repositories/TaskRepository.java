package com.example.springboot.repositories;


import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByBoardColumn_Id(Long taskListId);

    Task findTaskByBoardColumn_Id(Long taskListId);

    @Query("select t from Task t left join fetch t.comments where t.id = :id")
    Optional<Task> findByIdWithComments(Long id);

    // Подсчет задач с определенным статусом по проекту IN_PROGRESS, PENDING

    // Нахождение среднего времени выполнения задач для проекта
}
