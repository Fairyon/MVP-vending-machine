package com.example.vendingmachine.service;

import com.example.vendingmachine.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product findById(Long productId);

    Product saveProduct(Product product);

    List<Product> findAllProducts();

    Product updateProduct(Product product);

    boolean deleteProduct(Long productId);
}
