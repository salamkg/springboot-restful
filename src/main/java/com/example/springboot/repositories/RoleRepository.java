package com.example.springboot.repositories;


import com.example.springboot.models.entities.Project;
import com.example.springboot.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
