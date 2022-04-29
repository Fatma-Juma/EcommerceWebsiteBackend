package com.simple.ecommerce.repository;

import com.simple.ecommerce.models.Order;
import com.simple.ecommerce.models.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends DataTablesRepository<Order, Long> {
    Optional<Order> findById(Long id);
    List<Order> findAllByUser(User user);

}
