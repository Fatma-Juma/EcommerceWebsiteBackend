package com.simple.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.simple.ecommerce.models.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u where u.type = 2")
  List<User> getAllCustomers();
  
  @Query("SELECT u FROM User u where u.type = 1")
  List<User> getAllAdmins(DataTablesInput input);

  Optional<User> findById(Long id);

  Boolean existsByUsername(String username);
}
