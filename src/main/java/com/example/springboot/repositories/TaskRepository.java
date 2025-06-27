package com.example.springboot.repositories;


import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskList_Id(Long taskListId);

    Task findTaskByTaskList_Id(Long taskListId);

    // Подсчет задач с определенным статусом по проекту IN_PROGRESS, PENDING

    // Нахождение среднего времени выполнения задач для проекта
}
