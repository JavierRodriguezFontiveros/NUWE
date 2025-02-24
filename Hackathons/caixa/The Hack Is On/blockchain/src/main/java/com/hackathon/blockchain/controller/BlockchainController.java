package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    @Autowired
    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    // Publicar un bloque minado
    @PostMapping("/mina")
    public String mineBlock(@RequestParam String data) {
        return blockchainService.mineBlock(data);
    }

    // Obtener la cadena de bloques
    @GetMapping
    public List<Block> getBlockchain() {
        return blockRepository.findAll(Sort.by(Sort.Direction.ASC, "blockIndex"));
    }

    // Validar la cadena de bloques
    @GetMapping("/validar")
    public String validateBlockchain() {
        boolean isValid = blockchainService.isChainValid();
        return "{\"message\": \"Blockchain valid: " + isValid + "\"}";
    }
}
