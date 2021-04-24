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
@Table(name = "users")
@Data
public class User {
    @Id
    int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}
