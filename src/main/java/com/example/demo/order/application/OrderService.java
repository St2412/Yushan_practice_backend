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

    /**
     * 查詢可購買商品，並過濾掉無庫存資料。
     *
     * @return 可購買商品清單
     */
    public List<AvailableProductResponse> getAvailableProducts() {
        return orderRepository.getAvailableProducts().stream()
                .filter(product -> product.quantity() != null && product.quantity() > 0)
                .toList();
    }

    /**
     * 依會員編號試算訂單，回傳項目明細與總價。
     *
     * @param memberId 會員編號
     * @return 訂單試算結果
     */
    public OrderPreviewResponse previewOrder(String memberId) {
        List<OrderPreviewItemResponse> items = orderRepository.getMemberOrderItems(memberId);
        int totalPrice = items.stream().mapToInt(OrderPreviewItemResponse::itemPrice).sum();
        return new OrderPreviewResponse(memberId, items, totalPrice);
    }

    /**
     * 建立訂單主檔，並回傳訂單結果。
     *
     * @param request 訂單建立請求資料
     * @return 訂單建立結果
     * @throws BusinessException 當訂單資料序列化失敗或資料庫商業規則檢核失敗時拋出
     */
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

    /**
     * 將訂單明細序列化為 SP 所需的 JSON 字串。
     *
     * @param request 訂單建立請求資料
     * @return 訂單明細 JSON 字串
     * @throws BusinessException 當訂單明細序列化失敗時拋出
     */
    private String toItemsJson(CreateOrderRequest request) {
        try {
            return objectMapper.writeValueAsString(request.getItems());
        } catch (JsonProcessingException exception) {
            throw new BusinessException("建立訂單失敗", "ORDER_SERIALIZATION_ERROR");
        }
    }

    /**
     * 將資料層例外轉換為統一的商業例外，避免直接暴露資料庫錯誤細節。
     *
     * @param exception 資料層例外
     * @return 對應的商業例外
     */
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
