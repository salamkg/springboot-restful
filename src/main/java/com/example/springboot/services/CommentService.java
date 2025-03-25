package com.example.springboot.services;

import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;

import java.util.List;

public interface CommentService {

    String addComment(Long taskId, Long userId, Comment comment);
    List<Comment> getTaskComments(Long taskId);
}
