package com.example.springboot.repositories;


import com.example.springboot.models.entities.ChangeLog;
import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
//    Optional<ChangeLog> findByBoardId(Long boardId);
//    Optional<ChangeLog> findByTaskListId(Long taskListId);
}
