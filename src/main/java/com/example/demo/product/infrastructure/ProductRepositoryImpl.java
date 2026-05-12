package com.example.demo.product.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createProductBySp(String productId, String productName, Integer price, Integer quantity) {
        jdbcTemplate.update("CALL sp_create_product(?, ?, ?, ?)", productId, productName, price, quantity);
    }
}
