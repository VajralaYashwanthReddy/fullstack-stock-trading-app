package com.trading.dto;

public class ChangePasswordRequest {

    private Long userId;
    private String newPassword;

    // ===== GETTERS =====

    public Long getUserId() {
        return userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    // ===== SETTERS =====

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
