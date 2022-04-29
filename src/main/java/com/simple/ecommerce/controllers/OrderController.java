package com.simple.ecommerce.controllers;

import com.simple.ecommerce.models.*;
import com.simple.ecommerce.payload.response.MessageResponse;
import com.simple.ecommerce.repository.*;
import com.simple.ecommerce.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedProductsRepo orderedProductsRepo;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/all")
    public ResponseEntity<?> create() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        List<Cart> cartList = cartRepository.findAllByUser(user);

        if (!cartList.isEmpty()) {
            Order order = new Order();
            order.setUser(user);
            orderRepository.save(order);

            double total = 0;
            for (Cart cart : cartList) {
                Product product = null;
                try {
                    product = productRepository.getById(cart.getProduct().getId());
                    total = total + (product.getPrice() * cart.getQty());
                } catch (EntityNotFoundException entityNotFoundException) {
                    return ResponseEntity.ok(new MessageResponse("Product is Invalid"));
                }
                OrderedProducts orderedProducts = new OrderedProducts();
                orderedProducts.setProduct(product);
                orderedProducts.setPrice(product.getPrice()*cart.getQty());
                orderedProducts.setQty(cart.getQty());
                orderedProducts.setId(new Random().nextLong());
                orderedProducts.setOrders(order);
                orderedProductsRepo.save(orderedProducts);
                cartRepository.delete(cart);
            }
            order.setTotal(total);
            orderRepository.save(order);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No products in cart"));
        }
        return ResponseEntity.ok(new MessageResponse("Order Placed Successfully"));
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<?> getByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return ResponseEntity.ok(orderRepository.findAllByUser(user));
    }

    @PostMapping("/get/all")
    public DataTablesOutput<?> getAllOrders(@Valid @RequestBody DataTablesInput input) {
        return orderRepository.findAll(input);
    }

    @PostMapping("/get/all-by-user")
    public DataTablesOutput<?> getAllOrdersByUser(@Valid @RequestBody DataTablesInput input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return orderRepository.findAll(input, onlyLoggedInUserData(userDetails.getId()));
    }

    public static Specification<Order> onlyLoggedInUserData(Long user_id) {
        return (orderRoot, query, builder) -> builder.equal(orderRoot.get("user").get("id"), user_id);
    }

}
