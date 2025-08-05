package com.example.springboot.repositories;

import com.example.springboot.models.entities.Board;
import com.example.springboot.models.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> findAllByBoard_Id(Long boardId);

    BoardColumn findBoardColumnByNameIgnoreCase(String name);

    BoardColumn findByIdAndName(Long id, String name);

    Optional<BoardColumn> findByBoardAndNameIgnoreCase(Board board, String newStatusName);
}
