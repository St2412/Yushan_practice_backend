package com.example.demo.order.dto;

public record OrderPreviewItemResponse(
        String productId,
        String productName,
        Integer quantity,
        Integer standPrice,
        Integer itemPrice
) {
}
