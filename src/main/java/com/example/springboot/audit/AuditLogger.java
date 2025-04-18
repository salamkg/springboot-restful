package com.example.springboot.audit;

import com.example.springboot.repositories.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogger {

    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Before("execution(* com.example.springboot.services.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Вход в метод: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "execution(* com.example.springboot.services.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Выход из метода: " + joinPoint.getSignature().getName());
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}


}
