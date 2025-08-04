package com.example.springboot.audit;

import com.example.springboot.models.dto.*;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.services.ActivityLogService;
import com.example.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogService activityLogService;
    private final UserService userService;

    @Around("@annotation(activityLog)")
    public Object logActivityMethod(ProceedingJoinPoint joinPoint, ActivityLog activityLog) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long entityId = null;
        Long userId = userService.getCurrentUserId();

        for (Object arg : args) {
            if (arg instanceof Long id) {
                entityId = id; // Может быть ID удаляемой сущности
            }
        }

        Object result = joinPoint.proceed(); // Выполнить метод

        if (result instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();
            if (body instanceof ProjectDto dto) {
                entityId = dto.getId();
            } else if (body instanceof BoardDto board) {
                entityId = board.getId();
            } else if (body instanceof BoardColumnDTO boardColumn) {
                entityId = boardColumn.getId();
            } else if (body instanceof TaskDto task) {
                entityId = task.getId();
            }
        }
        activityLogService.logActivity(
                userId,
                entityId,
                activityLog.entity(),
                activityLog.type()
        );

        return result;
    }
}

