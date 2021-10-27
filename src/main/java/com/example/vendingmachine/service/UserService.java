package com.example.vendingmachine.service;

import com.example.vendingmachine.model.entity.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findById(Long userId);

    User saveUser(User user);

    List<User> findAllUsers();

    User updateUser(User user);

    boolean deleteUser(Long userId);
}