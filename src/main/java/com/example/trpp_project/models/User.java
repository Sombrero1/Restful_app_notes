package com.example.trpp_project.models;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<com.example.trpp_project.models.Table> tables;

}
