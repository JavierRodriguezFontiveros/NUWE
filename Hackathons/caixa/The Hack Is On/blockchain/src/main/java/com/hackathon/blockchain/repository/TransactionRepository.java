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

    // Encuentra transacciones realizadas desde una wallet asociada a un usuario específico
    List<Transaction> findBySenderWallet_UserId(Long userId);

    // Encuentra transacciones recibidas por una wallet asociada a un usuario específico
    List<Transaction> findByReceiverWallet_UserId(Long userId);

    // Método para encontrar transacciones por el ID del usuario de la billetera
    List<Transaction> findByWallet_UserId(Long userId);  // Esto debería solucionar el problema
}
