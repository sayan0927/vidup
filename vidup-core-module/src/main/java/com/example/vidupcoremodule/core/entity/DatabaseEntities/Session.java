package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Table(name = "session")
@Data
public class Session {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    private User user;

    @Column(name = "username",unique = true)
    String userName;

    @Column(name = "login_time")
    Time loginTime;

    @Column(name = "ip_address")
    String ipAddress;

    @Column(name = "token",unique = true)
    String token;

}
