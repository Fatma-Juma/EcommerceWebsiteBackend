package com.simple.ecommerce.controllers;

import com.simple.ecommerce.models.Cart;
import com.simple.ecommerce.models.Product;
import com.simple.ecommerce.models.User;
import com.simple.ecommerce.payload.request.CartAdd;
import com.simple.ecommerce.payload.response.MessageResponse;
import com.simple.ecommerce.repository.CartRepository;
import com.simple.ecommerce.repository.ProductRepository;
import com.simple.ecommerce.repository.UserRepository;
import com.simple.ecommerce.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/cart")
@RestController
public class CartController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody CartAdd cartAdd) {
        Product product = null;
        try {
            product = productRepository.getById(cartAdd.getProductId());
        } catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.ok(new MessageResponse("Product Variation is Invalid"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cart cart = cartRepository.findAllByUserAndProduct(user, product);
        if (cart != null) {
            cart.setProduct(product);
            cart.setQty(cartAdd.getQuantity() + cart.getQty());
            cart.setUser(user);
        } else {
            cart = new Cart();
            cart.setProduct(product);
            cart.setQty(cartAdd.getQuantity());
            cart.setUser(user);
        }
        cartRepository.save(cart);

        return ResponseEntity.ok(new MessageResponse(product.getName() + " added to cart successfully"));
    }


    @GetMapping("/get")
    public ResponseEntity<?> get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        List<Cart> cartList = cartRepository.findAllByUser(user);
        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        Product product = null;
        try {
            product = productRepository.getById(productId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok(new MessageResponse("Product ID is Invalid"));
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cart cart = cartRepository.findAllByUserAndProduct(user, product);
        if (cart != null) {
            cartRepository.delete(cart);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Product does not exists on cart"));
        }

        return ResponseEntity.ok(new MessageResponse("Product removed successfully"));
    }


    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CartAdd cartAdd) {
        Product product = null;
        try {
            product = productRepository.getById(cartAdd.getProductId());
        } catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.ok(new MessageResponse("Product Variation " + product.getName() + " is Invalid"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cart cart = cartRepository.findAllByUserAndProduct(user, product);
        if (cart != null) {
            cart.setProduct(product);
            cart.setQty(cartAdd.getQuantity());
            cart.setUser(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Update failed "));
        }
        cartRepository.save(cart);

        return ResponseEntity.ok(new MessageResponse("Product Quantity updated successfully"));
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<?> clearCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        List<Cart> cart = cartRepository.findAllByUser(user);

        if (!cart.isEmpty()) {
            cartRepository.deleteAll(cart);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No Product found on cart "));
        }

        return ResponseEntity.ok(new MessageResponse("Cart cleared successfully"));
    }
}
