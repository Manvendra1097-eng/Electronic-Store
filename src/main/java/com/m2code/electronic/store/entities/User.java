package com.m2code.electronic.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String userId;

    private String name;

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    @Column(length = 1000)
    private String about;

    private String gender;

    private String imageName;
}
