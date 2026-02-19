package com.trading.dto;

public class PortfolioResponse {

    private String symbol;
    private int quantity;
    private double currentPrice;
    private double currentValue;
    private double averagePrice;
    private double profitLoss;

    public PortfolioResponse(String symbol,
                             int quantity,
                             double currentPrice,
                             double currentValue,
                             double averagePrice,
                             double profitLoss) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.currentValue = currentValue;
        this.averagePrice = averagePrice;
        this.profitLoss = profitLoss;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public double getProfitLoss() {
        return profitLoss;
    }
}
