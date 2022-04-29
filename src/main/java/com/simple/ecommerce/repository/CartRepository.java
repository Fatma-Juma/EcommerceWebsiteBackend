package com.simple.ecommerce.repository;

import com.simple.ecommerce.models.Cart;
import com.simple.ecommerce.models.Product;
import com.simple.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUser(User user);

    Cart findAllByUserAndProduct(User user, Product product);

}
