package com.hackathon.blockchain.repository;

import com.hackathon.blockchain.model.Transaction;
import com.hackathon.blockchain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Encuentra transacciones realizadas desde una wallet específica
    List<Transaction> findBySenderWallet(Wallet senderWallet);

    // Encuentra transacciones recibidas por una wallet específica
    List<Transaction> findByReceiverWallet(Wallet receiverWallet);

    // Encuentra transacciones por su estado (ej. "Pending", "Completed")
    List<Transaction> findByStatus(String status);
}
