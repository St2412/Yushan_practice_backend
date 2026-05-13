package com.example.demo.order.application;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.order.dto.AvailableProductResponse;
import com.example.demo.order.dto.CreateOrderRequest;
import com.example.demo.order.dto.CreateOrderResponse;
import com.example.demo.order.dto.OrderPreviewItemResponse;
import com.example.demo.order.dto.OrderPreviewResponse;
import com.example.demo.order.infrastructure.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final DateTimeFormatter ORDER_ID_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public List<AvailableProductResponse> getAvailableProducts() {
        return orderRepository.getAvailableProducts().stream()
                .filter(product -> product.quantity() != null && product.quantity() > 0)
                .toList();
    }

    public OrderPreviewResponse previewOrder(String memberId) {
        List<OrderPreviewItemResponse> items = orderRepository.getMemberOrderItems(memberId);
        int totalPrice = items.stream().mapToInt(OrderPreviewItemResponse::itemPrice).sum();
        return new OrderPreviewResponse(memberId, items, totalPrice);
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        String orderId = "Ms" + LocalDateTime.now().format(ORDER_ID_FORMATTER)
                + ThreadLocalRandom.current().nextInt(100, 1000);
        String itemsJson = toItemsJson(request);

        try {
            orderRepository.createOrder(orderId, request.getMemberId(), itemsJson);
        } catch (DataAccessException exception) {
            throw mapDataAccessException(exception);
        }

        List<OrderPreviewItemResponse> items = orderRepository.previewOrder(itemsJson).stream()
                .map(item -> new OrderPreviewItemResponse(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.standPrice(),
                        item.itemPrice()))
                .toList();
        int totalPrice = items.stream().mapToInt(OrderPreviewItemResponse::itemPrice).sum();
        return new CreateOrderResponse(orderId, request.getMemberId(), 0, totalPrice, items);
    }

    private String toItemsJson(CreateOrderRequest request) {
        try {
            return objectMapper.writeValueAsString(request.getItems());
        } catch (JsonProcessingException exception) {
            throw new BusinessException("建立訂單失敗", "ORDER_SERIALIZATION_ERROR");
        }
    }

    private BusinessException mapDataAccessException(DataAccessException exception) {
        String message = exception.getMostSpecificCause() != null
                ? exception.getMostSpecificCause().getMessage()
                : exception.getMessage();

        if (message != null && message.contains("PRODUCT_NOT_FOUND")) {
            return new BusinessException("商品不存在", "PRODUCT_NOT_FOUND");
        }
        if (message != null && message.contains("INVALID_ORDER_QUANTITY")) {
            return new BusinessException("數量錯誤", "INVALID_ORDER_QUANTITY");
        }
        if (message != null && message.contains("OUT_OF_STOCK")) {
            return new BusinessException("商品庫存不足", "OUT_OF_STOCK");
        }
        if (message != null && message.contains("EMPTY_ORDER_ITEMS")) {
            return new BusinessException("訂單項目為空", "EMPTY_ORDER_ITEMS");
        }

        return new BusinessException("Stored Procedure 執行失敗", "SP_EXECUTION_FAILED");
    }
}
