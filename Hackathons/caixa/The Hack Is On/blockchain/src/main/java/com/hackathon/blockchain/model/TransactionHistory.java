package com.hackathon.blockchain.model;

import java.util.List;

public class TransactionHistory {
    private List<Transaction> sent;
    private List<Transaction> received;

    // Getters y setters
    public List<Transaction> getSent() {
        return sent;
    }

    public void setSent(List<Transaction> sent) {
        this.sent = sent;
    }

    public List<Transaction> getReceived() {
        return received;
    }

    public void setReceived(List<Transaction> received) {
        this.received = received;
    }
}
