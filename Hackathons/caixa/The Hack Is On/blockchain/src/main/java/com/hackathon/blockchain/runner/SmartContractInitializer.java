package com.hackathon.blockchain.runner;

import com.hackathon.blockchain.model.Wallet;
import com.hackathon.blockchain.service.SmartContractService;
import com.hackathon.blockchain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmartContractInitializer {

    @Autowired
    private SmartContractService smartContractService;

    @Autowired
    private WalletRepository walletRepository; // Inyectamos el repositorio de Wallet

    public void initSmartContracts() {
        // Obtener la wallet desde el repositorio por su ID
        Long issuerWalletId = 123456L;
        Wallet associatedWallet = walletRepository.findById(issuerWalletId)
                .orElseThrow(() -> new RuntimeException("Wallet no encontrada"));

        // Crear el contrato inteligente pasando el Wallet obtenido
        try {
            smartContractService.createSmartContract("ContractName", "ContractDescription", 
                                                     "ConditionExpression", "Action", 
                                                     associatedWallet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
