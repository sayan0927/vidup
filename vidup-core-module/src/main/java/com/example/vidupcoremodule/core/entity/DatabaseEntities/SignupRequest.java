package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

@Entity
@Table(name = "signup_request")
@Data
public class SignupRequest {

    @PrePersist
    protected void init() {
        validTill = LocalDateTime.now().plus(Duration.ofMinutes(30));
    }
    @Id
    @Column(name = "email",unique = true)
    String email;

    @Column(name = "fname")
    String fName;

    @Column(name = "lname")
    String lName;

    @Column(name = "age")
    Integer age;

    @Column(name = "sex")
    String sex;

    @Column(name = "contact")
    String contact;

    @Column(name = "username",unique = true)
    String userName;

    @Column(name = "password")
    String password;

    @Column(name = "profile_image",nullable = true)
    byte[] profileImage;

    @Column(name = "activation_token")
    String activationToken;

    @Column(name = "valid_till")
    LocalDateTime validTill;

}
