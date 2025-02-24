package com.hackathon.blockchain.repository;

import com.hackathon.blockchain.model.WalletKey;
import com.hackathon.blockchain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletKeyRepository extends JpaRepository<WalletKey, String> {
    Optional<WalletKey> findByWallet(Wallet wallet);  // Nuevo método para encontrar por Wallet
    Optional<WalletKey> findByWalletId(Long walletId);  // Ya existente
}
