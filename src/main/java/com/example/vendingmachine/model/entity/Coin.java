package com.example.vendingmachine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "coins")
public class Coin {

    @JsonIgnore
    @Transient
    public static final int [] POSSIBLE_VALUES = {100, 50, 20, 10, 5};

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", unique = true)
    private Integer value;

    @Column(name = "amount")
    private Integer amount = 0;

    public Coin(Integer value, Integer amount) {
        this.value = value;
        this.amount = amount;
    }

    public Coin() {
        // JPA
    }

    public Long getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}