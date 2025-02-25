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
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;  // Agregar esta importación para el logger

@Entity
@Slf4j  // Asegurarse de agregar esta anotación
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
    public Wallet(String address, double balance, User user) {
        this.address = address;
        this.balance = balance;
        this.netWorth = 0.0;  // Valor predeterminado
        this.user = user;     // Asociamos el wallet con un user
    }

    // Método para generar el par de claves RSA
    public void generateKeys() throws Exception {
        log.info("Iniciando generación de claves RSA...");  // Usar el log creado por la anotación @Slf4j
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // Longitud de la clave
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        if (keyPair.getPublic() == null || keyPair.getPrivate() == null) {
            log.error("Error: Claves RSA generadas son nulas");
            throw new RuntimeException("Error: Claves RSA generadas son nulas");
        }

        this.publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        this.privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        log.info("Claves RSA generadas correctamente.");
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

    // Nuevo método para obtener el userId desde la Wallet
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
