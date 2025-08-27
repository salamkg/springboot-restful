package com.example.springboot.repositories;


import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.Project;
import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByBoardColumn_Id(Long taskListId);

    Task findTaskByBoardColumn_Id(Long taskListId);

    @Query("select t from Task t left join fetch t.comments where t.id = :id")
    Optional<Task> findByIdWithComments(Long id);

    List<Task> findTasksByBoardAndBoard_Id(Board board, Long boardId);

    List<Task> findTasksByBoard_Id(Long boardId);

    @Query("select count(t) from Task t where t.board.project.id = :projectId")
    Long countByProjectId(Long projectId);

    Optional<Task> findTaskByKey(String key);

    @Query("select t from Task t " +
            "join fetch t.boardColumn bc " +
            "join fetch bc.board b " +
            "join fetch b.project p " +
            "where p.key = :projectKey and b.id = :boardId")
    List<Task> findTasksByProjectKeyAndBoardId(String projectKey, Long boardId);

//    @Query("select t from Task t where t.project.key = :projectKey")
    List<Task> findAllByProject(Project project);

    @Query("select t from Task t where t.status = 'DONE'")
    List<Task> findAllDoneLast7Days();

    @Query(value = "select * from tasks t where t.created_at >= now() - interval '7 days'", nativeQuery = true)
    List<Task> findAllCreatedLast7Days();

    @Query(value = "select * from tasks t where t.updated_at >= now() - interval '7 days'", nativeQuery = true)
    List<Task> findAllUpdatedLast7Days();

    // Подсчет задач с определенным статусом по проекту IN_PROGRESS, PENDING

    // Нахождение среднего времени выполнения задач для проекта
}
