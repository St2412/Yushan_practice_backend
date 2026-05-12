package com.example.demo.product.application;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.product.domain.Product;
import com.example.demo.product.infrastructure.ProductRepository;
import com.example.demo.product.presentation.dto.CreateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        if (productRepository.existsByProductId(request.getProductId())) {
            throw new BusinessException("商品編號已存在");
        }

        Product product = new Product(
                request.getProductId(),
                request.getProductName(),
                request.getPrice(),
                request.getQuantity());

        return productRepository.save(product);
    }
}
