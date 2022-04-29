package com.simple.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ordered_products")
public class OrderedProducts {
    @Id
    private Long id;

    private double qty;
    private double price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orders;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
