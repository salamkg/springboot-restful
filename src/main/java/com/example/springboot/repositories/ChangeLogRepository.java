package com.example.springboot.repositories;


import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<Task, Long> {

}
