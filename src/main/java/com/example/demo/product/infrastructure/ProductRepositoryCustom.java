package com.example.demo.product.infrastructure;

public interface ProductRepositoryCustom {

    void createProductBySp(String productId, String productName, Integer price, Integer quantity);
}
