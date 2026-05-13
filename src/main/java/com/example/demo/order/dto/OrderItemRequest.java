package com.example.demo.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {

    @NotBlank
    private String productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
