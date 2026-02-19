package com.trading.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddFundsRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;   // ✅ Use Double NOT double

    // ======== GETTERS ========

    public Long getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    // ======== SETTERS ========

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
