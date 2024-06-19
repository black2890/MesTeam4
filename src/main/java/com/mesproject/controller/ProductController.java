package com.mesproject.controller;

import com.mesproject.entity.Product;
import com.mesproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/get-products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
