package com.example.springboot.repositories;

import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    List<TaskList> findByBoardId(Long boardId);
}
