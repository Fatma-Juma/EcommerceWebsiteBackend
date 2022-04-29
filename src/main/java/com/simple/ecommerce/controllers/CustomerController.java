package com.simple.ecommerce.controllers;

import com.simple.ecommerce.exception.ResourceNotFoundException;
import com.simple.ecommerce.models.Category;
import com.simple.ecommerce.models.Product;
import com.simple.ecommerce.payload.response.MessageResponse;
import com.simple.ecommerce.repository.CategoryRepository;
import com.simple.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/product/get/all")
    public ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/category/get/all")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/get-by-cat/{catID}")
    public ResponseEntity<?> getByCategory(@PathVariable Long catID) {
        if (catID.equals(0L)) {
            return ResponseEntity.ok(productRepository.findAll());
        }
        Category category = categoryRepository.findById(catID).isPresent() ? categoryRepository.findById(catID).get() : null;
        if (category != null) {
            return ResponseEntity.ok(productRepository.findByCategory(category));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Category Id does not exist!!"));
    }

}
