package com.hackathon.blockchain.model;

import java.util.Map;

public class WalletDetails {
    private String walletAddress;
    private double cashBalance;
    private double netWorth;
    private Map<String, Double> assets;

    // Constructor con parámetros
    public WalletDetails(double cashBalance, double netWorth, Map<String, Double> assets) {
        this.cashBalance = cashBalance;
        this.netWorth = netWorth;
        this.assets = assets;
    }

    // Getters y setters
    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public Map<String, Double> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Double> assets) {
        this.assets = assets;
    }
}
