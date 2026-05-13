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
    public List<OrderPreviewItemResponse> getMemberOrderItems(String memberId) {
        return jdbcTemplate.query(
                """
                SELECT od.product_id,
                       p.product_name,
                       od.quantity,
                       od.stand_price,
                       od.item_price
                FROM orders o
                JOIN order_detail od ON o.order_id = od.order_id
                JOIN product p ON p.product_id = od.product_id
                WHERE o.member_id = ?
                ORDER BY od.order_id, od.order_item_sn
                """,
                (rs, rowNum) -> new OrderPreviewItemResponse(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getInt("stand_price"),
                        rs.getInt("item_price")),
                memberId);
    }

    @Override
    public void createOrder(String orderId, String memberId, String itemsJson) {
        jdbcTemplate.update("CALL sp_create_order(?, ?, ?)", orderId, memberId, itemsJson);
    }
}
