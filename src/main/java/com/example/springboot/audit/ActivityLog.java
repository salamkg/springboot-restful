package com.example.springboot.audit;

import com.example.springboot.models.enums.EntityType;
import com.example.springboot.models.enums.ActivityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLog {
    ActivityType type();
    EntityType entity();
}
