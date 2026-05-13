package com.example.demo.order.presentation;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.order.application.OrderService;
import com.example.demo.order.dto.CreateOrderRequest;
import com.example.demo.order.dto.CreateOrderResponse;
import com.example.demo.order.dto.OrderPreviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/preview")
    public ApiResponse<OrderPreviewResponse> preview(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.previewOrder(request));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.createOrder(request));
    }
}
