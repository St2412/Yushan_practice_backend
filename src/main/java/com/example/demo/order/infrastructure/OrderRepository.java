package com.example.demo.order.infrastructure;

import com.example.demo.order.dto.AvailableProductResponse;
import com.example.demo.order.dto.OrderPreviewItemResponse;
import java.util.List;

public interface OrderRepository {

    List<AvailableProductResponse> getAvailableProducts();

    List<OrderPreviewItemResponse> previewOrder(String itemsJson);

    List<OrderPreviewItemResponse> getMemberOrderItems(String memberId);

    void createOrder(String orderId, String memberId, String itemsJson);
}
