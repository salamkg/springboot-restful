package com.example.springboot.repositories;

import com.example.springboot.models.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> findAllByBoard_Id(Long boardId);

    BoardColumn findBoardColumnByNameContainingIgnoreCase(String name);
}
