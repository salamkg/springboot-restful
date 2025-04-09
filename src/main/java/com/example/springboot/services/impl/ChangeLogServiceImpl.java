package com.example.springboot.services.impl;

import com.example.springboot.models.entities.ChangeLog;
import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import com.example.springboot.repositories.ChangeLogRepository;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.ChangeLogService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChangeLogServiceImpl implements ChangeLogService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;
    @Autowired
    private UserService userService;

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

    @Override
    public void saveChangeLog(Task newTask, Task oldTask, String action) {
        String firstName;
        firstName = userRepository.findByUsername(userService.getCurrentUser()).get().getFirstName();
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityId(newTask.getId());
        changeLog.setEntityType("Task");
        String changesJson = makeChanges(newTask, oldTask);

        changeLog.setAction(action);
        changeLog.setEntityId(newTask.getId() != null ? newTask.getId() : oldTask.getId());
        changeLog.setEntityType("Task");
        changeLog.setChanges(changesJson);
        changeLog.setChangedBy(newTask.getAuthor() != null ? newTask.getAuthor().getUsername() : oldTask.getAuthor().getUsername());
        changeLog.setChangedAt(new Date());

        boolean isChanged = false;

        if ("create".equals(action)) {
            changeLog.setAttributeNames("all");
            changeLog.setChanges(changesJson);
            isChanged = true;

        } else if (oldTask != null && "edit".equals(action) || "delete".equals(action)) {
            if (newTask.getName() != null && !newTask.getName().equals(oldTask.getName())) {
                changeLog.setAttributeNames("name"); //TODO remove or update attribute_names
                isChanged = true;
            }
            if (newTask.getDescription() != null && !newTask.getDescription().equals(oldTask.getDescription())) {
                changeLog.setAttributeNames("description");
                isChanged = true;
            }
            if (newTask.getPosition() != null && !newTask.getPosition().equals(oldTask.getPosition())) {
                changeLog.setAttributeNames("position");
                isChanged = true;
            }
            if (newTask.getPriority() != null && !newTask.getPriority().equals(oldTask.getPriority())) {
                changeLog.setAttributeNames("priority");
                isChanged = true;
            }
            if (newTask.getStatus() != null && !newTask.getStatus().equals(oldTask.getStatus())) {
                changeLog.setAttributeNames("status");
                isChanged = true;
            }
        }

        if (isChanged) {
            changeLogRepository.save(changeLog);
        }
    }
}
