package com.example.springboot.services.impl;

import com.example.springboot.mappers.ActivityLogMapper;
import com.example.springboot.models.dto.ActivityLogDto;
import com.example.springboot.models.entities.ActivityLog;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.ActivityLogRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.ActivityLogService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityLogServiceImpl{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityLogMapper activityLogMapper;

    public String makeChanges(Task newTask, Task oldTask) {
        //TODO realize Optimistic Locking, without blocking of edits from Users
        try {
            Map<String, Map<String, String>> changes = new HashMap<>();

            changes.put("name", Map.of("old", oldTask != null ? oldTask.getName() : "","new", newTask.getName()));
            changes.put("description", Map.of("old", oldTask != null ? oldTask.getDescription() : "","new", newTask.getDescription()));
            changes.put("status", Map.of("old", oldTask != null ? oldTask.getStatus().toString() : "","new", newTask.getStatus().toString()));
            changes.put("priority", Map.of("old", oldTask != null ? oldTask.getPriority() : "","new", newTask.getPriority()));
            changes.put("assignees", Map.of("old", oldTask != null ? oldTask.getAssignedUsers().stream().map(User::getUsername).toString() : "","new", newTask.getAssignedUsers().stream().map(User::getUsername).toString()));
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(changes);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


//    @Override
//    public List<ActivityLogDto> getTaskHistory(Long taskId, String sort) {
//        return activityLogRepository.findAllByTaskId(taskId)
//                .stream()
//                .map(ch -> activityLogMapper.toChangeLogDto(ch))
//                .toList();
//    }
}
