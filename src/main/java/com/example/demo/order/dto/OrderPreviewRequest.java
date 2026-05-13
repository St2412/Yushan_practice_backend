package com.example.demo.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderPreviewRequest(
        @NotBlank(message = "memberId 為必填")
        String memberId
) {
}
