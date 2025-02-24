package com.hackathon.blockchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática de ID
    private Long id;

    private String address;
    private double balance;
    private double netWorth;
    private String accountStatus = "Active";  // Valor por defecto

    // Campos para las claves RSA
    private String publicKey;
    private String privateKey;

    @ManyToOne  // Relación con User
    @JoinColumn(name = "user_id")  // Especificamos la columna que representa la relación
    private User user;

    @OneToMany(mappedBy = "wallet")  // Relación con Asset
    private List<Asset> assets = new ArrayList<>();

    // Constructor vacío (requerido por JPA)
    public Wallet() {
    }

    // Constructor con parámetros
    public Wallet(String address, double balance) {
        this.address = address;
        this.balance = balance;
        this.netWorth = 0.0;  // Valor predeterminado
    }

    // Método para generar el par de claves RSA
    public void generateKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // Longitud de la clave
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Guardamos las claves en formato Base64 para almacenarlas como cadenas de texto
        this.publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        this.privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    // Métodos para obtener las claves
    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
