package com.simple.ecommerce.repository;

import com.simple.ecommerce.models.Category;
import com.simple.ecommerce.models.Product;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends DataTablesRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Product getById(Long id);

    List<Product> findAll();

    List<Product> findByCategory(Category category);
}
