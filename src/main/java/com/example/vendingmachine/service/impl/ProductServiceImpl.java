package com.example.vendingmachine.service.impl;

import com.example.vendingmachine.dao.ProductRepository;
import com.example.vendingmachine.model.entity.Product;
import com.example.vendingmachine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        return null;
    }

    @Override
    public Product saveProduct(Product product) {

        return (Product) productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Product product) {

        return (Product) productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(Long productId) {
        try {
            productRepository.deleteById(productId);
            return true;
        } catch(EmptyResultDataAccessException e) {
            return false;
        }
    }
}
