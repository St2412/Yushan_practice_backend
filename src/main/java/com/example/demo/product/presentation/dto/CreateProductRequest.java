package com.example.demo.product.presentation.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {

    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
