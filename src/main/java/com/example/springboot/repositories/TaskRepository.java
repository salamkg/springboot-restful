package com.example.springboot.repositories;


import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Подсчет задач с определенным статусом по проекту IN_PROGRESS, PENDING

    // Нахождение среднего времени выполнения задач для проекта
}
