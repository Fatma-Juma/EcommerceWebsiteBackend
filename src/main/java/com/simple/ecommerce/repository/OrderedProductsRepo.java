package com.simple.ecommerce.repository;

import com.simple.ecommerce.models.OrderedProducts;
import com.simple.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedProductsRepo extends JpaRepository<OrderedProducts, Long> {
	List<OrderedProducts> findByProduct(Product product);
}
