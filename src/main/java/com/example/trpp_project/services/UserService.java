package com.example.trpp_project.services;

import com.example.trpp_project.models.User;

import java.util.List;



public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
