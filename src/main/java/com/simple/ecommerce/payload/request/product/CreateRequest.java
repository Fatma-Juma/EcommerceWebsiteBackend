package com.simple.ecommerce.payload.request.product;

import lombok.Data;

@Data
public class CreateRequest {
    private String name;
    private Double price;
    private Long catID;
    private byte[] image;
}
