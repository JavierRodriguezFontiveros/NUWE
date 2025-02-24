package com.hackathon.blockchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String txId;  // ID único de transacción
    private double amount;  // Cambié 'amount' a double para representar la cantidad de forma más precisa
    private String type;  // Tipo de la transacción (puede ser "Transfer", "Deposit", etc.)
    private String status;  // Estado de la transacción (ej. "Pending", "Completed")
    private String description;  // Descripción opcional de la transacción
    
    // Nuevas variables agregadas
    private String senderAddress; // Dirección del remitente
    private String receiverAddress; // Dirección del receptor
    private Date transactionDate; // Fecha de la transacción
    
    @ManyToOne  // Relación con Wallet si es una entidad
    @JoinColumn(name = "sender_wallet_id")  // Relación con la columna en la base de datos
    private Wallet senderWallet;

    @ManyToOne  // Relación con Wallet si es una entidad
    @JoinColumn(name = "destination_wallet_id")  // Relación con la columna en la base de datos
    private Wallet destinationWallet;

    // Constructor vacío para JPA
    public Transaction() {
    }

    // Constructor con parámetros ajustado
    public Transaction(String txId, String senderAddress, String receiverAddress, Wallet sender, Wallet receiver, String symbol, double quantity, double price, String type, Date transactionDate) {
        this.txId = txId;
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.senderWallet = sender;
        this.destinationWallet = receiver;
        this.amount = quantity * price; // Se calcula el monto usando cantidad y precio
        this.type = type;
        this.status = "PENDING"; // Suponiendo que el estado inicial es "PENDING"
        this.description = symbol; // Opcional, podrías ajustar cómo manejar la descripción
        this.transactionDate = transactionDate; // Si quieres almacenar la fecha
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public double getAmount() {
        return amount;  // Ahora es un valor numérico de tipo double
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Wallet getDestinationWallet() {
        return destinationWallet;
    }

    public void setDestinationWallet(Wallet destinationWallet) {
        this.destinationWallet = destinationWallet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Nuevos Getters y Setters
    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Agregamos el método getSender() que probablemente estabas buscando
    public Wallet getSender() {
        return senderWallet;
    }

    // Si necesitas el destinatario
    public Wallet getReceiver() {
        return destinationWallet;
    }
}
