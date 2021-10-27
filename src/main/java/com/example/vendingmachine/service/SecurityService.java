package com.example.vendingmachine.service;

import com.example.vendingmachine.model.entity.User;

public interface SecurityService {
    User findLoggedInUser();
}