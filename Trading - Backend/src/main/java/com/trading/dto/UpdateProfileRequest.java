package com.trading.dto;

public class UpdateProfileRequest {

    private String username;
    private String email;

    // ===== GETTERS =====

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // ===== SETTERS =====

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
