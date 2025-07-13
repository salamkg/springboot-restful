package com.example.springboot.repositories;

import com.example.springboot.models.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> findByBoardId(Long boardId);
    Optional<BoardColumn> findByTasks_Id(Long taskId);

    List<BoardColumn> findAllByBoard_Id(Long boardId);
}
