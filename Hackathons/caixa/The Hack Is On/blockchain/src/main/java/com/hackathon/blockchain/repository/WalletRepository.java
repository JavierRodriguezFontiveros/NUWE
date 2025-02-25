package com.hackathon.blockchain.repository;

import com.hackathon.blockchain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Método personalizado para buscar una Wallet por su dirección
    Optional<Wallet> findByAddress(String address);

    // Método personalizado para buscar una Wallet por el ID del usuario asociado
    Optional<Wallet> findByUser_Id(Long userId);
}
