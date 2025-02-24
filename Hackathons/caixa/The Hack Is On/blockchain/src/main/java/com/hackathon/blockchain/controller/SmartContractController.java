package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.model.SmartContract;
import com.hackathon.blockchain.service.SmartContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hackathon.blockchain.dto.CreateSmartContractRequest;


@RestController
@RequestMapping("/contracts")
public class SmartContractController {

    @Autowired
    private SmartContractService smartContractService;

    @PostMapping("/create")
    public SmartContract createSmartContract(@RequestBody CreateSmartContractRequest request) throws Exception {
        return smartContractService.createSmartContract(
                request.getContractId(), 
                request.getContractName(),
                request.getConditionExpression(),
                request.getAction(),
                request.getActionValue(),
                request.getIssuerWalletId());
    }

    @GetMapping("/validate/{contractId}")
    public String validateSmartContract(@PathVariable Long contractId) throws Exception {
        return smartContractService.validateSmartContract(contractId);
    }
}
