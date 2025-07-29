package com.example.springboot.repositories;


import com.example.springboot.models.entities.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
