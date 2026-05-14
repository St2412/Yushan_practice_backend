package com.example.demo.order.presentation;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.order.application.OrderService;
import com.example.demo.order.dto.CreateOrderRequest;
import com.example.demo.order.dto.CreateOrderResponse;
import com.example.demo.order.dto.OrderPreviewRequest;
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

    /**
     * 試算會員目前購物車（或待結算項目）的金額與明細。
     *
     * @param request 訂單試算請求資料
     * @return 訂單試算結果的 API 回應
     */
    @PostMapping("/preview")
    public ApiResponse<OrderPreviewResponse> preview(@Valid @RequestBody OrderPreviewRequest request) {
        return ApiResponse.success(orderService.previewOrder(request.memberId()));
    }

    /**
     * 建立正式訂單。
     *
     * @param request 訂單建立請求資料
     * @return 訂單建立結果的 API 回應
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.createOrder(request));
    }
}
