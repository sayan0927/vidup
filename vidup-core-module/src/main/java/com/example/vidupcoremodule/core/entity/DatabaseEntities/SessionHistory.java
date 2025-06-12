package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Table(name = "session_history")
@Data
public class SessionHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "username")
    String userName;

    @Column(name = "login_time")
    Time loginTime;


    @Column(name = "logout_time")
    Time logoutTime;

    @Column(name = "ip_address")
    String ipAddress;

}
