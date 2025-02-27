package com.example.springboot.services.impl;

import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.CommentRepository;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.CommentService;
import com.example.springboot.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Comment addComment(Long taskId, Long userId, Comment text) {
        System.out.println(text);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

//        Comment comment = new Comment();
        text.setTask(task);
        text.setUser(user);
//        comment.setText(text);

        return commentRepository.save(text);
    }

    @Override
    public List<Comment> getTaskComments(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
