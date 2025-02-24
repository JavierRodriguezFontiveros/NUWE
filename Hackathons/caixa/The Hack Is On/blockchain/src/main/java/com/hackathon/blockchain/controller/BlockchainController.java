package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.model.Block;
import com.hackathon.blockchain.model.Transaction;
import com.hackathon.blockchain.repository.BlockRepository;
import com.hackathon.blockchain.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final BlockRepository blockRepository;

    @Autowired
    public BlockchainController(BlockchainService blockchainService, BlockRepository blockRepository) {
        this.blockchainService = blockchainService;
        this.blockRepository = blockRepository;
    }

    // Publicar un bloque minado
    @PostMapping("/mina")
    public String mineBlock() {
        return blockchainService.mineBlock();
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

    // Agregar una transacción pendiente
    @PostMapping("/transaccion")
    public String addTransaction(@RequestBody Transaction transaction) {
        blockchainService.addTransaction(transaction);
        return "{\"message\": \"Transaction added to pending list.\"}";
    }
}
