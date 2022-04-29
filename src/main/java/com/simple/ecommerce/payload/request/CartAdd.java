package com.simple.ecommerce.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartAdd {
    private Long productId;
    private Integer quantity;
}
