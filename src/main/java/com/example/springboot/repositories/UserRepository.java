package com.example.springboot.repositories;


import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    //  Найти всех пользователей, которые участвуют в проектах, в которых есть хотя бы одна задача с
    //  количеством комментариев больше 10 и с определенным статусом (например, "IN_PROGRESS")
}
