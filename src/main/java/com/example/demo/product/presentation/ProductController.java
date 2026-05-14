package com.example.demo.product.presentation;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.order.application.OrderService;
import com.example.demo.order.dto.AvailableProductResponse;
import com.example.demo.product.application.ProductService;
import com.example.demo.product.domain.Product;
import com.example.demo.product.presentation.dto.CreateProductRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    /**
     * 建立新商品資料。
     *
     * @param request 商品建立請求資料
     * @return 包含建立完成商品的 API 回應
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Product> create(@Valid @RequestBody CreateProductRequest request) {
        return ApiResponse.success(productService.createProduct(request));
    }

    /**
     * 取得目前可購買（庫存大於 0）的商品清單。
     *
     * @return 可購買商品清單的 API 回應
     */
    @GetMapping("/available")
    public ApiResponse<List<AvailableProductResponse>> getAvailableProducts() {
        return ApiResponse.success(orderService.getAvailableProducts());
    }
}
