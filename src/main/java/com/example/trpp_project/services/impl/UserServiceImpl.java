package com.example.trpp_project.services.impl;

import com.example.trpp_project.models.User;
import com.example.trpp_project.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userRepository.findByUsername(username);
        if (user == null){
            log.info("not found user with username: {}",username);
            throw new UsernameNotFoundException(username);
        }
        log.info("Loaded user with username: {}",username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

//    @Override
//    public User register(User user) {
//        Role roleUser = roleRepository.findByName("ROLE_USER");
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleUser);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(userRoles);
//        user.setStatus(Status.ACTIVE);
//
//        User registeredUser = userRepository.save(user);
//
//        log.info("IN register - user: {} successfully registered", registeredUser);
//
//        return registeredUser;
//    }
//
//    @Override
//    public List<User> getAll() {
//        List<User> result = userRepository.findAll();
//        log.info("IN getAll - {} users found", result.size());
//        return result;
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        User result = userRepository.findByUsername(username);
//        log.info("IN findByUsername - user: {} found by username: {}", result, username);
//        return result;
//    }
//
//    @Override
//    public User findById(Long id) {
//        User result = userRepository.findById(id).orElse(null);
//
//        if (result == null) {
//            log.warn("IN findById - no user found by id: {}", id);
//            return null;
//        }
//
//        log.info("IN findById - user: {} found by id: {}", result);
//        return result;
//    }
//
//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//        log.info("IN delete - user with id: {} successfully deleted");
//    }
}
