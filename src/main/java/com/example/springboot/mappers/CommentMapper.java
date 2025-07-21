package com.example.springboot.mappers;

import com.example.springboot.models.dto.CommentDto;
import com.example.springboot.models.entities.Comment;

public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);
}
