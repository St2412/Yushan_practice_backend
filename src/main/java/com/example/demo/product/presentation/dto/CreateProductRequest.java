package com.example.demo.product.presentation.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "商品編號為必填")
    @Pattern(regexp = "^[A-Za-z0-9_-]{1,50}$", message = "商品編號格式錯誤")
    private String productId;

    @NotBlank(message = "商品名稱為必填")
    @Size(max = 100, message = "商品名稱長度不可超過 100")
    @Pattern(regexp = "^[^<>]*$", message = "商品名稱不可包含 HTML 標籤字元")
    private String productName;

    @NotNull(message = "價格為必填")
    @DecimalMin(value = "1", message = "價格必須大於 0")
    private BigDecimal price;

    @NotNull(message = "庫存為必填")
    @Min(value = 0, message = "庫存不可小於 0")
    private Integer quantity;
}
