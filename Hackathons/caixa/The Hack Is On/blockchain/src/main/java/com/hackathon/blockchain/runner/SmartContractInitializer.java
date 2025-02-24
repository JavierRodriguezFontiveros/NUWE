package com.hackathon.blockchain.runner;

import com.hackathon.blockchain.service.SmartContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SmartContractInitializer implements CommandLineRunner {

    @Autowired
    private SmartContractService smartContractService;

    @Override
    public void run(String... args) throws Exception {
        // Crear automáticamente el contrato inteligente para la billetera de liquidez BTC
        smartContractService.createSmartContract(
                "BTC-Liquidity-AntiWhale", 
                "Anti-whale contract for BTC", 
                "#amount > 10000", 
                "CANCEL_TRANSACTION", 
                "0.0", 
                "1");  // "1" es el walletId de ejemplo
    }
}
