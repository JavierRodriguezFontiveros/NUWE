package com.hackathon.blockchain.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")  // Relación con Wallet
    private List<Wallet> wallets;

    // Constructor vacío
    public User() {
    }

    // Constructor con solo el username
    public User(String username) {
        this.username = username;
    }

    // Constructor con parámetros completos
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.setPassword(password);  // Establece la contraseña encriptada
    }

    // Getter y setter para el id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter y setter para el username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter y setter para el email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter y setter para la password con hash
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);  // Encripta la contraseña antes de almacenarla
    }

    // Getter y setter para los Wallets asociados a este User
    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }
}
