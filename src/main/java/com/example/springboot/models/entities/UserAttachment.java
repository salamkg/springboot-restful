package com.example.springboot.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_attachments")
@Builder
public class UserAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String filename;
    private String fileType;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
