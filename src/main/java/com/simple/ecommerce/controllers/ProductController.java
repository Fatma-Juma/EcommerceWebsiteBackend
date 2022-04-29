package com.simple.ecommerce.controllers;

import com.simple.ecommerce.exception.ResourceNotFoundException;
import com.simple.ecommerce.models.Category;
import com.simple.ecommerce.models.Product;
import com.simple.ecommerce.payload.request.product.CreateRequest;
import com.simple.ecommerce.payload.response.MessageResponse;
import com.simple.ecommerce.repository.CategoryRepository;
import com.simple.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateRequest createRequest) {
        Category category = categoryRepository.findById(createRequest.getCatID()).isPresent() ? categoryRepository.findById(createRequest.getCatID()).get() : null;
        if (category != null) {
            Product product = new Product(createRequest.getName(), createRequest.getPrice(), category, createRequest.getImage());
            productRepository.save(product);
            return ResponseEntity.ok(new MessageResponse("Product Added Successfully"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Failed to add product"));
    }

    @PostMapping("/get/all")
    public DataTablesOutput<?> getAllProduct(@Valid @RequestBody DataTablesInput input) {
        return productRepository.findAll(input);
    }


    @GetMapping("/get/all/cat")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/get-by-cat/{catID}")
    public ResponseEntity<?> getByCategory(@PathVariable Long catID) {
        Category category = categoryRepository.findById(catID).isPresent() ? categoryRepository.findById(catID).get() : null;
        if (category != null) {
            return ResponseEntity.ok(productRepository.findByCategory(category));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Category Id does not exist!!"));
    }

    @GetMapping("/get-by-id/{id}")
    public Product getProductById(@PathVariable(value = "id")long id){
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateRequest updateRequest) {
        Product product = productRepository.findById(id).isPresent() ? productRepository.findById(id).get() : null;
        HashMap<String, Object> response = new HashMap<>();

        Category category = categoryRepository.findById(updateRequest.getCatID()).isPresent() ? categoryRepository.findById(updateRequest.getCatID()).get() : null;
        if (product != null) {
            product.setName(updateRequest.getName());
            product.setPrice(updateRequest.getPrice());
            product.setCategory(category);
            productRepository.save(product);
            return ResponseEntity.ok(new MessageResponse("Product Updated Successfully"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Failed to update product"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Product product = productRepository.findById(id).isPresent() ? productRepository.findById(id).get() : null;

        if (product != null) {
            productRepository.delete(product);
            return ResponseEntity.ok(new MessageResponse("Product Deleted Successfully"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Failed to delete product"));
    }
}
