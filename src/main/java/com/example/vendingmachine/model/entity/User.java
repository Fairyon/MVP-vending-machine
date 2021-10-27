package com.example.vendingmachine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @JsonIgnore
    @Transient
    public static final String[] POSSIBLE_ROLES = {"admin", "seller", "buyer"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "role")
    private String role;

    @JsonIgnore
    public Integer currentAmount = 0;

    public String getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long userId) {
        this.id = userId;
    }
}