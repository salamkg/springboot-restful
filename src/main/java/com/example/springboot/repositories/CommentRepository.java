package com.example.springboot.repositories;


import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTaskId(Long taskId);

    Comment findByParent_Id(Long parentId);

    // Найти все комментарии, сделанные пользователями с userId, которые относятся к задачам в проектах, в которых
    // есть хотя бы одна завершенная задача и хотя бы один комментарий с определенным текстом.
}
