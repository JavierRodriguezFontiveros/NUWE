package com.hackathon.blockchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Block {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;  // Datos del bloque
    private String previousHash;  // Hash del bloque anterior
    private String hash;  // Hash del bloque actual
    private int blockIndex;  // Índice del bloque

    // Constructor
    public Block(String data, String previousHash, String hash, int blockIndex) {
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.blockIndex = blockIndex;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    // Método para calcular el hash (utiliza un ejemplo simple)
    public String calculateHash() {
        // Asegúrate de utilizar un algoritmo adecuado en producción (ej. SHA-256)
        return Integer.toHexString((previousHash + data).hashCode());
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", hash='" + hash + '\'' +
                ", blockIndex=" + blockIndex +
                '}';
    }
}
