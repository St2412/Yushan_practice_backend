package com.example.demo.order.dto;

import java.util.List;

public record CreateOrderResponse(
        String orderId,
        String memberId,
        Integer payStatus,
        Integer totalPrice,
        List<OrderPreviewItemResponse> items
) {
}
