package com.simple.ecommerce.repository;

import com.simple.ecommerce.models.Category;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends DataTablesRepository<Category, Long> {
    Optional<Category> findById(Long id);

}
