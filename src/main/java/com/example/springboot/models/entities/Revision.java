package com.example.springboot.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@Entity
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
