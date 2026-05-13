package com.example.demo.order.dto;

import java.util.List;

public record OrderPreviewResponse(
        String memberId,
        List<OrderPreviewItemResponse> items,
        Integer totalPrice
) {
}
