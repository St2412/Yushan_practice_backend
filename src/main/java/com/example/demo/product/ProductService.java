package com.example.demo.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        if (productRepository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("商品編號已存在");
        }

        Product product = new Product(
                request.getProductId(),
                request.getProductName(),
                request.getPrice(),
                request.getQuantity());

        return productRepository.save(product);
    }
}
