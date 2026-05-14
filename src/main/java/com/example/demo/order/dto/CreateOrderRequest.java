package com.example.demo.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]{1,50}$", message = "memberId 格式錯誤")
    private String memberId;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;
}
