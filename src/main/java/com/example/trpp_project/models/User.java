package com.example.trpp_project.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * Simple domain object that represents application user.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Entity
@Table(name = "users",uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
@Data
public class User {
    @Id
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Card> cards;



}
