package com.example.springboot.repositories;

import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    List<TaskList> findByBoardId(Long boardId);
    Optional<TaskList> findByTasks_Id(Long taskId);
}
