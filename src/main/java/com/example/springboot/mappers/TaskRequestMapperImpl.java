package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.dto.TaskDto;
import com.example.springboot.models.entities.Comment;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.BoardColumn;
import com.example.springboot.repositories.BoardColumnRepository;
import com.example.springboot.repositories.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TaskRequestMapperImpl implements TaskRequestMapper {
    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnRequestMapper boardColumnRequestMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public TaskRequestMapperImpl(BoardColumnRepository boardColumnRepository,
                                 BoardColumnRequestMapper boardColumnRequestMapper,
                                 CommentMapper commentMapper, CommentRepository commentRepository) {
        this.boardColumnRepository = boardColumnRepository;
        this.boardColumnRequestMapper = boardColumnRequestMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }

        BoardColumn boardColumn = boardColumnRepository.findById(task.getBoardColumn().getId()).orElseThrow(() -> new RuntimeException("TaskList Not Found"));

        TaskDto.TaskDtoBuilder taskDto = TaskDto.builder();
        taskDto.id(task.getId());
        taskDto.name(task.getName());
        taskDto.description(task.getDescription());
        taskDto.priority(task.getPriority());
        taskDto.key(task.getKey());
        taskDto.status(task.getStatus());
        taskDto.boardColumnDTO(boardColumnRequestMapper.toBoardColumnDto(boardColumn));
        taskDto.comments(task.getComments() != null ? buildCommentTree(task.getComments()) : null);

        return taskDto.build();
    }

    private List<CommentDto> buildCommentTree(List<Comment> comments) {

        Map<Long, CommentDto> map = new HashMap<>();
        List<CommentDto> roots = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto commentDto = commentMapper.toCommentDto(comment);
            commentDto.setReplies(new ArrayList<>());
            map.put(commentDto.getId(), commentDto);
        }

        for (Comment comment : comments) {
            CommentDto commentDto = map.get(comment.getId());

            if (commentDto.getParentId() != null) {
                CommentDto parent = map.get(commentDto.getParentId());
                if (parent.getReplies() == null) {
                    parent.setReplies(new ArrayList<>());
                }
                parent.getReplies().add(commentDto);
            } else {
                roots.add(commentDto);
            }
        }
        return roots;
    }
}
