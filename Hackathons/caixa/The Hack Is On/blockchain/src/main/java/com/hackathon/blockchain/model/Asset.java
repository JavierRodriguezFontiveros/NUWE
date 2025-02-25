package com.hackathon.blockchain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY)  // Relación con Wallet
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)  // Relación con SmartContract
    @JoinColumn(name = "contract_id", nullable = false)  // El nombre de la columna en la base de datos
    private SmartContract smartContract;

    // Constructor sin ID porque se genera automáticamente
    public Asset(String symbol, double quantity, Wallet wallet, SmartContract smartContract) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.wallet = wallet;
        this.smartContract = smartContract;  // Asociamos el contrato inteligente
    }

    // Método getValue() para obtener el valor de la cantidad de activos
    public double getValue() {
        // Aquí puedes definir la lógica para obtener el valor de un activo.
        return quantity; // Reemplazar con lógica real si es necesario
    }

    // Métodos getter y setter para la propiedad symbol
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    // Métodos getter y setter para la propiedad quantity
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    // Método para obtener la wallet asociada
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    // Método para obtener el contrato asociado
    public SmartContract getSmartContract() {
        return smartContract;
    }

    public void setSmartContract(SmartContract smartContract) {
        this.smartContract = smartContract;
    }
}
