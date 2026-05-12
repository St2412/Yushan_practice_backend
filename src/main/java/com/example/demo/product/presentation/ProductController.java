package com.example.demo.product.presentation;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.product.application.ProductService;
import com.example.demo.product.domain.Product;
import com.example.demo.product.presentation.dto.CreateProductRequest;
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
=======
import lombok.RequiredArgsConstructor;
>>>>>>> main
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
=======
@RequiredArgsConstructor
>>>>>>> main
public class ProductController {

    private final ProductService productService;

<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

=======
>>>>>>> main
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Product> create(@RequestBody CreateProductRequest request) {
        return ApiResponse.success(productService.createProduct(request));
    }
}
