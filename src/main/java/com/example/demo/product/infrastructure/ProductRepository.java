package com.example.demo.product.infrastructure;

import com.example.demo.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByProductId(String productId);
}
