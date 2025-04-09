package com.example.springboot.repositories;


import com.example.springboot.models.entities.ChangeLog;
import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
    @Query("select ch from ChangeLog ch " +
            "where ch.entityId = :taskId ")
    List<ChangeLog> findAllByTaskId(Long taskId);
//    Optional<ChangeLog> findByBoardId(Long boardId);
//    Optional<ChangeLog> findByTaskListId(Long taskListId);

    // private String entityType;
    //    private Long entityId;
    //    private String action; // Тип изменения
    //    private String changedBy;
    //
    //    @Column(columnDefinition = "TEXT")
    //    private String changes;
}
