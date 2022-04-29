package com.simple.ecommerce.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Category category;

    public Product(String name, Double price, Category category, byte[] image) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
    }

    public Product() {

    }
}
