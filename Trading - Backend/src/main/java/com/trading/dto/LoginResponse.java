package com.trading.dto;

public class LoginResponse {

    private Long id;
    private String username;
    private String email;
    private double walletBalance;
    private String token;

    // 🔥 IMPORTANT CONSTRUCTOR
    public LoginResponse(Long id, String username, String email, double walletBalance, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.walletBalance = walletBalance;
        this.token = token;
    }

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public String getToken() {
        return token;
    }
}
