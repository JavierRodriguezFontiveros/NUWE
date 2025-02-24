package com.hackathon.blockchain.exceptions;

public class KeyGenerationException extends Exception {
    public KeyGenerationException() {
        super();
    }

    public KeyGenerationException(String message) {
        super(message);
    }

    public KeyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyGenerationException(Throwable cause) {
        super(cause);
    }
}
