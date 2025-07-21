package com.example.springboot.services.impl;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.CommentRepository;
import com.example.springboot.repositories.TaskRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public String addComment(Long taskId, Long userId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Comment newComment = new Comment();
        newComment.setTask(task);
        newComment.setUser(user);
        newComment.setText(commentDto.getText());
        newComment.setCreatedAt(new Date());

        if (commentDto.getParentId() != null) {
            Comment parent = commentRepository.findById(commentDto.getParentId()).orElseThrow(() -> new RuntimeException("Parent not found"));
            newComment.setParent(parent);
            parent.getReplies().add(newComment);
        }

        commentRepository.save(newComment);

        return "Comment successfully added!";
    }

    @Override
    public List<Comment> getTaskComments(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
