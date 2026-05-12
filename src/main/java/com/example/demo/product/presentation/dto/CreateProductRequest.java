package com.example.demo.product.presentation.dto;

import java.math.BigDecimal;
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo

=======
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
>>>>>>> main
public class CreateProductRequest {

    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
<<<<<<< codex/check-code-structure-against-design-requirements-2e8ixo

    public CreateProductRequest() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
=======
>>>>>>> main
}
