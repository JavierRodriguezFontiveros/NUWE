package com.hackathon.blockchain.response;

public class ResponseMessage {
    private String message;

    // Constructor
    public ResponseMessage(String message) {
        this.message = message;
    }

    // Getter y Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
