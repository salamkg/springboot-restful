package com.example.springboot.repositories;

import com.example.springboot.models.entities.UserAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttachmentRepository extends JpaRepository<UserAttachment, Long> {
}
