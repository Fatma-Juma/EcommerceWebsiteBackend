package com.simple.ecommerce.controllers;


import com.simple.ecommerce.exception.ResourceNotFoundException;
import com.simple.ecommerce.models.Category;
import com.simple.ecommerce.payload.request.category.CreateRequest;
import com.simple.ecommerce.payload.response.MessageResponse;
import com.simple.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;


    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateRequest createRequest) {
        Category category = new Category();
        category.setName(createRequest.getName());
        categoryRepository.save(category);
        return ResponseEntity.ok(new MessageResponse("Category Created Successfully"));
    }

    @GetMapping("/get/{id}")
    public Category getCategoryById(@PathVariable(value = "id")long id){
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id :" + id));
    }


    @PostMapping("/get/all")
    public DataTablesOutput<?> getAllCategory(@Valid @RequestBody DataTablesInput input) {
        return categoryRepository.findAll(input);
    }


    public static Specification<Category> onlyLoggedInUserData(Long user_id) {
        return (purchaseRoot, query, builder) -> builder.equal(purchaseRoot.get("user").get("id"), user_id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateRequest updateRequest) {
        Category category = categoryRepository.findById(id).isPresent() ? categoryRepository.findById(id).get() : null;

        if (category != null) {
            category.setName(updateRequest.getName());
            categoryRepository.save(category);
            return ResponseEntity.ok(new MessageResponse("Updated successfully"));

        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Update failed "));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).isPresent() ? categoryRepository.findById(id).get() : null;

        if (category != null) {
            categoryRepository.delete(category);
            return ResponseEntity.ok(new MessageResponse("Deleted Successfully"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Delete failed "));
    }



}
