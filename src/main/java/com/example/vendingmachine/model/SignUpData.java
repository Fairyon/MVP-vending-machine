package com.example.vendingmachine.model;

import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotBlank;

public class SignUpData {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

}