package com.hackathon.blockchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WalletKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática de ID
    private Long id;

    @ManyToOne  // Relación de muchos a uno con Wallet (un Wallet puede tener muchas WalletKeys)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")  // Columna que mapea la relación
    private Wallet wallet;

    private String publicKey;
    private String privateKey;  // Agregamos el campo para privateKey

    // Constructor vacío (requerido para JPA)
    public WalletKey() {
    }

    // Constructor con parámetros
    public WalletKey(Wallet wallet, String publicKey, String privateKey) {
        this.wallet = wallet;
        this.publicKey = publicKey;
        this.privateKey = privateKey;  // Inicializamos también privateKey
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Wallet getWallet() { return wallet; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
}
