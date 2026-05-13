package com.example.demo.order.infrastructure;

import com.example.demo.order.dto.AvailableProductResponse;
import com.example.demo.order.dto.OrderPreviewItemResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AvailableProductResponse> getAvailableProducts() {
        return jdbcTemplate.query(
                "CALL sp_get_available_products()",
                (rs, rowNum) -> new AvailableProductResponse(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("quantity")));
    }

    @Override
    public List<OrderPreviewItemResponse> previewOrder(String itemsJson) {
        return jdbcTemplate.query(
                "CALL sp_preview_order(?)",
                (rs, rowNum) -> new OrderPreviewItemResponse(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getInt("stand_price"),
                        rs.getInt("item_price")),
                itemsJson);
    }

    @Override
    public void createOrder(String orderId, String memberId, String itemsJson) {
        jdbcTemplate.update("CALL sp_create_order(?, ?, ?)", orderId, memberId, itemsJson);
    }
}
