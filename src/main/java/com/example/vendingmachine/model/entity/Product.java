package com.example.vendingmachine.model.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "amount_available")
    private Integer amountAvailable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="seller_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User seller;

    public Long getId() {
        return id;
    }

    public void setId(Long productId) {
        id = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Long getSellerId() {
        return seller.getId();
    }


    public void setSeller(User user) {
        seller = user;
    }
}