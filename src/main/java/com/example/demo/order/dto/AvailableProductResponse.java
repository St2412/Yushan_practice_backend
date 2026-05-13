package com.example.demo.order.dto;

public record AvailableProductResponse(
        String productId,
        String productName,
        Integer price,
        Integer quantity
) {
}
