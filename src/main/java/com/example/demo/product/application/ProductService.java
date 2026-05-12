package com.example.demo.product.application;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.product.domain.Product;
import com.example.demo.product.infrastructure.ProductRepository;
import com.example.demo.product.presentation.dto.CreateProductRequest;
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
=======
import lombok.RequiredArgsConstructor;
>>>>>>> main
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
=======
@RequiredArgsConstructor
>>>>>>> main
public class ProductService {

    private final ProductRepository productRepository;

<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

=======
>>>>>>> main
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
