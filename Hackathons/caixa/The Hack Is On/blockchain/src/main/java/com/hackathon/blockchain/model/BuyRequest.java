package com.hackathon.blockchain.model;

public class BuyRequest {
    private String symbol;
    private double quantity;

    // Getters y setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
