package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.model.SmartContract;
import com.hackathon.blockchain.service.SmartContractService;
import com.hackathon.blockchain.model.Wallet;
import com.hackathon.blockchain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hackathon.blockchain.dto.CreateSmartContractRequest;

@RestController
@RequestMapping("/contracts")
public class SmartContractController {

    @Autowired
    private SmartContractService smartContractService;

    @Autowired
    private WalletRepository walletRepository;  // Inyectar el WalletRepository

    @PostMapping("/create")
    public SmartContract createSmartContract(@RequestBody CreateSmartContractRequest request) throws Exception {
        // Convertimos issuerWalletId a Long
        Long issuerWalletId = Long.valueOf(request.getIssuerWalletId());

        // Obtenemos el Wallet usando el issuerWalletId
        Wallet wallet = walletRepository.findById(issuerWalletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Ahora pasamos el objeto Wallet al servicio
        return smartContractService.createSmartContract(
                request.getContractName(),
                request.getConditionExpression(),
                request.getAction(),
                request.getActionValue(),
                wallet  // Pasamos el Wallet en lugar de issuerWalletId
        );
    }

    @GetMapping("/validate/{contractId}")
    public String validateSmartContract(@PathVariable Long contractId) throws Exception {
        return smartContractService.validateSmartContract(contractId);
    }
}
