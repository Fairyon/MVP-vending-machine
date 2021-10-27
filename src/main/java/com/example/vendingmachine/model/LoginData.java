package com.example.vendingmachine.model;

import javax.validation.constraints.NotBlank;

public class LoginData {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}