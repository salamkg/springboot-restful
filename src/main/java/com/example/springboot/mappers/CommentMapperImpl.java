package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .text(comment.getText())
                .build();
    }
}
