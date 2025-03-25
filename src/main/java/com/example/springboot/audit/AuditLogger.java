package com.example.springboot.audit;

import com.example.springboot.models.entities.AuditLog;
import com.example.springboot.repositories.AuditLogRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class AuditLogger {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Before("execution(* com.example.springboot.services.TaskService.createTask(..))")
    public void logCreateTask() {
        AuditLog auditLog = new AuditLog();
        auditLog.setOperationType("CREATE");
        auditLog.setEntity("Task");
        auditLog.setUserId(1L);
        auditLog.setOldValue(null);
        auditLog.setNewValue("New task");
        auditLog.setTimestamp(new Date());

        auditLogRepository.save(auditLog);
    }
}
