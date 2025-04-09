package com.example.springboot.services;

import com.example.springboot.models.dto.ChangeLogDto;
import com.example.springboot.models.entities.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChangeLogService {
    void saveChangeLog(Task newTask, Task oldTask, String action);

    List<ChangeLogDto> getTaskHistory(Long taskId, String sort);
}
