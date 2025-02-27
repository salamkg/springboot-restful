package com.example.springboot.controllers;

import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/{userId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId, @PathVariable Long userId, @RequestBody Comment text) {

        Comment comment = commentService.addComment(taskId, userId, text);

        return ResponseEntity.ok().body(comment);
    }

    @GetMapping()
    public ResponseEntity<List<Comment>> getTaskComments(@PathVariable Long taskId) {
        List<Comment> allComments = commentService.getTaskComments(taskId);

        return ResponseEntity.ok().body(allComments);
    }
}
