package com.example.vendingmachine.controller;

import com.example.vendingmachine.model.entity.Product;
import com.example.vendingmachine.model.entity.User;
import com.example.vendingmachine.service.ProductService;
import com.example.vendingmachine.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid Product product) {
        User user = securityService.findLoggedInUser();
        if(user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if(user.getRole() != "seller")
            return new ResponseEntity<Object>("User is not a seller.", HttpStatus.UNAUTHORIZED);

        product.setSeller(user);
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {
        User user = securityService.findLoggedInUser();
        if(user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if(user.getRole() != "seller")
            return new ResponseEntity<Object>("User is not a seller.", HttpStatus.UNAUTHORIZED);

        Product oldProduct = productService.findById(productId);
        if(oldProduct == null)
            return new ResponseEntity<Object>("Product does not exist.", HttpStatus.NOT_FOUND);

        if(product.getSellerId() != user.getId())
            return new ResponseEntity<Object>("User is not an owner of the product.", HttpStatus.UNAUTHORIZED);

        product.setId(productId);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId) {
        User user = securityService.findLoggedInUser();
        if(user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if(user.getRole() != "seller")
            return new ResponseEntity<Object>("User is not a seller.", HttpStatus.UNAUTHORIZED);

        Product product = productService.findById(productId);
        if(product == null)
            return new ResponseEntity<Object>("Product does not exist.", HttpStatus.NOT_FOUND);

        if(product.getSellerId() != user.getId())
            return new ResponseEntity<Object>("User is not an owner of the product.", HttpStatus.UNAUTHORIZED);

        if(productService.deleteProduct(productId)) {
            return ResponseEntity.ok("Product '" + productId + "' has been deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

}