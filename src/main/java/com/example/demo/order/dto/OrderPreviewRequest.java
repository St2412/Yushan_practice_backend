package com.example.demo.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OrderPreviewRequest(
        @NotBlank(message = "memberId 為必填")
        @Pattern(regexp = "^[A-Za-z0-9_-]{1,50}$", message = "memberId 格式錯誤")
        String memberId
) {
}
