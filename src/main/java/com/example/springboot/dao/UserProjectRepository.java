package com.example.springboot.dao;

import com.example.springboot.models.entities.Project;
import com.example.springboot.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProjectRepository extends JpaRepository<Project, Long> {

//    @Query(value = "select p.id, p.title from Project p join user_project up on p.id = up.project_id where up.user_id = :userId")

//    List<Project> findProjectsByUserId(Long userId);
}
