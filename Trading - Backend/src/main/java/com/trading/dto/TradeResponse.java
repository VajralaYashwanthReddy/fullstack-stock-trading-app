package com.trading.dto;

public class TradeResponse {

    private String message;
    private double price;
    private double totalAmount;
    private double walletBalance;   // ✅ ADD THIS

    public TradeResponse(String message, double price, double totalAmount, double walletBalance) {
        this.message = message;
        this.price = price;
        this.totalAmount = totalAmount;
        this.walletBalance = walletBalance;
    }

    public String getMessage() { return message; }
    public double getPrice() { return price; }
    public double getTotalAmount() { return totalAmount; }
    public double getWalletBalance() { return walletBalance; }
}
