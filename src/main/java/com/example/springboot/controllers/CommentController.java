package com.example.springboot.controllers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/{userId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Добавить комментарий к задаче")
    @PostMapping()
    public String addComment(@PathVariable Long taskId, @PathVariable Long userId, @RequestBody CommentDto dto) {

        return commentService.addComment(taskId, userId, dto);
    }

    @GetMapping()
    public ResponseEntity<List<Comment>> getTaskComments(@PathVariable Long taskId) {
        List<Comment> allComments = commentService.getTaskComments(taskId);

        return ResponseEntity.ok().body(allComments);
    }
}
