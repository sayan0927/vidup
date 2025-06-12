package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="tag")
@Data
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    @Column(name = "tname",unique = true)
    String tagName;



}
